package com.chalnakchalnak.account.presentation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.S3Object;
import com.chalnakchalnak.account.profile.adapter.in.web.ProfileImageController;
import java.io.ByteArrayInputStream;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

class ProfileImageControllerTest {
  private ProfileImageController controller(AmazonS3 s3) {
    var storage =
        new com.chalnakchalnak.account.profile.adapter.out.storage.MinioProfileImageAdapter(s3);
    org.springframework.test.util.ReflectionTestUtils.setField(storage, "bucket", "images");
    return new ProfileImageController(
        new com.chalnakchalnak.account.profile.application.service.ProfileImageService(
            mock(com.chalnakchalnak.account.member.application.port.out.MemberRepositoryPort.class),
            storage));
  }

  private static final String KEY =
      "member/64c7f981-17f8-4dbf-95c7-fd854d30791f/images/626ef0dd-7c0f-4a67-94b8-a1fb6b439b70.png";

  @Test
  void servesProfileImageBytesFromConfiguredStorage() throws Exception {
    var s3 = mock(AmazonS3.class);
    var object = new S3Object();
    object.setObjectContent(new ByteArrayInputStream(new byte[] {1, 2, 3}));
    object.getObjectMetadata().setContentType("image/png");
    when(s3.getObject("images", KEY)).thenReturn(object);
    var result = controller(s3).read(KEY);
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(result.getHeaders().getContentType().toString()).isEqualTo("image/png");
    assertThat(result.getBody()).containsExactly((byte) 1, (byte) 2, (byte) 3);
  }

  @Test
  void rejectsNonProfileKeysWithoutReadingStorage() {
    var s3 = mock(AmazonS3.class);
    assertThatThrownBy(() -> controller(s3).read("../private.txt"))
        .isInstanceOf(ResponseStatusException.class);
    verifyNoInteractions(s3);
  }
}
