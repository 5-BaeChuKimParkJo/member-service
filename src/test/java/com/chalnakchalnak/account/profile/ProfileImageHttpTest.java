package com.chalnakchalnak.account.profile;

import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.S3Object;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest(
    properties =
        "spring.datasource.url=jdbc:h2:mem:profile-image-http;MODE=PostgreSQL;DB_CLOSE_DELAY=-1")
@AutoConfigureMockMvc(addFilters = false)
class ProfileImageHttpTest {
  private static final String KEY =
      "member/64c7f981-17f8-4dbf-95c7-fd854d30791f/images/626ef0dd-7c0f-4a67-94b8-a1fb6b439b70.png";
  private static final String ROUTE = "/api/v1/member/images";
  private static final int MAX_BYTES = 10 * 1024 * 1024;

  @Autowired MockMvc mvc;
  @MockBean AmazonS3 storage;

  @ParameterizedTest
  @ValueSource(
      strings = {
        "/api/v1/member/images",
        "/member-service/api/v1/member/images",
        "/account-service/api/v1/member/images"
      })
  void invalidKeysAre400OnEveryAliasWithoutReadingStorage(String route) throws Exception {
    for (String key :
        new String[] {"../private.txt", "", "member/temp_member_uuid/images/default.png"}) {
      mvc.perform(get(route).param("key", key))
          .andExpect(status().isBadRequest())
          .andExpect(jsonPath("$.code").value(400))
          .andExpect(header().string("Cache-Control", "no-store"));
    }
    verifyNoInteractions(storage);
  }

  @Test
  void missingRequiredKeyIs400WithoutReadingStorage() throws Exception {
    mvc.perform(get(ROUTE))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.code").value(400))
        .andExpect(header().string("Cache-Control", "no-store"));
    verifyNoInteractions(storage);
  }

  @ParameterizedTest
  @ValueSource(
      strings = {
        "/api/v1/member/images",
        "/member-service/api/v1/member/images",
        "/account-service/api/v1/member/images"
      })
  void missingObjectsAre404OnEveryAlias(String route) throws Exception {
    var failure = new AmazonS3Exception("fixture private storage host/key");
    failure.setStatusCode(404);
    when(storage.getObject("test-bucket", KEY)).thenThrow(failure);
    mvc.perform(get(route).param("key", KEY))
        .andExpect(status().isNotFound())
        .andExpect(
            content()
                .json(
                    """
            {"code":404,"message":"요청한 리소스를 찾을 수 없습니다."}
            """))
        .andExpect(jsonPath("$.httpStatus").doesNotExist())
        .andExpect(header().string("Cache-Control", "no-store"));
  }

  @ParameterizedTest
  @NullSource
  @ValueSource(strings = {"text/plain", "image/svg+xml"})
  void invalidObjectContentTypeIs400(String contentType) throws Exception {
    when(storage.getObject("test-bucket", KEY)).thenReturn(image(new byte[] {1}, contentType));
    mvc.perform(get(ROUTE).param("key", KEY))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.code").value(400))
        .andExpect(header().string("Cache-Control", "no-store"));
  }

  @Test
  void overLimitImageIs413WithoutImmutableCaching() throws Exception {
    when(storage.getObject("test-bucket", KEY))
        .thenReturn(image(new byte[MAX_BYTES + 1], "image/png"));
    mvc.perform(get(ROUTE).param("key", KEY))
        .andExpect(status().isPayloadTooLarge())
        .andExpect(jsonPath("$.code").value(413))
        .andExpect(header().string("Cache-Control", "no-store"));
  }

  @Test
  void exactLimitImageStillReturnsItsBytes() throws Exception {
    byte[] bytes = new byte[MAX_BYTES];
    when(storage.getObject("test-bucket", KEY)).thenReturn(image(bytes, "image/png"));
    mvc.perform(get(ROUTE).param("key", KEY))
        .andExpect(status().isOk())
        .andExpect(content().bytes(bytes));
  }

  @Test
  void successfulImagePreservesContentTypeBytesAndImmutableCachePolicy() throws Exception {
    byte[] bytes = new byte[] {1, 2, 3};
    when(storage.getObject("test-bucket", KEY)).thenReturn(image(bytes, "image/png"));
    mvc.perform(get("/member-service/api/v1/member/images").param("key", KEY))
        .andExpect(status().isOk())
        .andExpect(content().contentType("image/png"))
        .andExpect(content().bytes(bytes))
        .andExpect(header().string("Cache-Control", "max-age=31536000, public, immutable"));
  }

  @ParameterizedTest
  @ValueSource(ints = {403, 500, 503})
  void storageAccessAndServerFailuresRemainGeneric500(int upstreamStatus) throws Exception {
    var failure = new AmazonS3Exception("fixture private endpoint credential");
    failure.setStatusCode(upstreamStatus);
    when(storage.getObject("test-bucket", KEY)).thenThrow(failure);
    expectGenericServerError();
  }

  @Test
  void connectionFailureRemainsGeneric500() throws Exception {
    when(storage.getObject("test-bucket", KEY))
        .thenThrow(new SdkClientException("fixture private connection/password"));
    expectGenericServerError();
  }

  @Test
  void streamReadFailureRemainsGeneric500() throws Exception {
    var object = new S3Object();
    object.getObjectMetadata().setContentType("image/png");
    object.setObjectContent(
        new InputStream() {
          @Override
          public int read() throws IOException {
            throw new IOException("fixture private storage path");
          }
        });
    when(storage.getObject("test-bucket", KEY)).thenReturn(object);
    expectGenericServerError();
  }

  private void expectGenericServerError() throws Exception {
    mvc.perform(get(ROUTE).param("key", KEY))
        .andExpect(status().isInternalServerError())
        .andExpect(
            content()
                .json(
                    """
            {"code":500,"message":"서버 내부 오류가 발생했습니다. 관리자에게 문의해주세요."}
            """))
        .andExpect(header().string("Cache-Control", "no-store"));
  }

  private S3Object image(byte[] bytes, String contentType) {
    var object = new S3Object();
    object.setObjectContent(new ByteArrayInputStream(bytes));
    object.getObjectMetadata().setContentType(contentType);
    return object;
  }
}
