package com.chalnakchalnak.account.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import com.amazonaws.services.s3.AmazonS3;
import com.chalnakchalnak.account.profile.adapter.out.storage.MinioProfileImageAdapter;
import com.chalnakchalnak.account.profile.application.command.PrepareProfileImageCommand;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

class PresignedProfileStorageTest {
  @Test
  void usesConfiguredPathStyleMinioEndpointForProfileUploads() {
    var service = service();
    ReflectionTestUtils.setField(service, "endpoint", "http://minio:9000/");
    var signed =
        service.generatePresignedPost(
            PrepareProfileImageCommand.builder()
                .key("member/user/images/photo.png")
                .contentType("image/png")
                .build());
    assertThat(signed.getUrl()).isEqualTo("http://minio:9000/images/");
    assertThat(signed.getFields()).containsEntry("key", "member/user/images/photo.png");
    assertThat(signed.getFields()).containsEntry("Content-Type", "image/png");
  }

  @Test
  void retainsAwsEndpointWhenNoCompatibleEndpointIsConfigured() {
    var service = service();
    ReflectionTestUtils.setField(service, "endpoint", "");
    assertThat(
            service
                .generatePresignedPost(
                    PrepareProfileImageCommand.builder()
                        .key("member/user/images/photo.png")
                        .contentType("image/png")
                        .build())
                .getUrl())
        .isEqualTo("https://images.s3.ap-northeast-2.amazonaws.com/");
  }

  private MinioProfileImageAdapter service() {
    var service = new MinioProfileImageAdapter(mock(AmazonS3.class));
    ReflectionTestUtils.setField(service, "bucket", "images");
    ReflectionTestUtils.setField(service, "region", "ap-northeast-2");
    ReflectionTestUtils.setField(service, "accessKey", "test-key");
    ReflectionTestUtils.setField(service, "secretKey", "test-secret");
    return service;
  }
}
