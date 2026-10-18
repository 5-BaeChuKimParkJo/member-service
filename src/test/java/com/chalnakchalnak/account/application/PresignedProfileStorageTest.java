package com.chalnakchalnak.account.application;

import com.chalnakchalnak.account.dto.in.PresignedUrlRequestDto;
import com.chalnakchalnak.account.infrastructure.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class PresignedProfileStorageTest {
    @Test
    void usesConfiguredPathStyleMinioEndpointForProfileUploads() {
        var service = service();
        ReflectionTestUtils.setField(service, "endpoint", "http://minio:9000/");
        var signed = service.generatePresignedPost(PresignedUrlRequestDto.builder()
                .key("member/user/images/photo.png").contentType("image/png").build());
        assertThat(signed.getUrl()).isEqualTo("http://minio:9000/images/");
        assertThat(signed.getFields()).containsEntry("key", "member/user/images/photo.png");
        assertThat(signed.getFields()).containsEntry("Content-Type", "image/png");
    }

    @Test
    void retainsAwsEndpointWhenNoCompatibleEndpointIsConfigured() {
        var service = service();
        ReflectionTestUtils.setField(service, "endpoint", "");
        assertThat(service.generatePresignedPost(PresignedUrlRequestDto.builder()
                .key("member/user/images/photo.png").contentType("image/png").build()).getUrl())
                .isEqualTo("https://images.s3.ap-northeast-2.amazonaws.com/");
    }

    private PresignedUrlServiceImpl service() {
        var service = new PresignedUrlServiceImpl(mock(MemberRepository.class));
        ReflectionTestUtils.setField(service, "bucket", "images");
        ReflectionTestUtils.setField(service, "region", "ap-northeast-2");
        ReflectionTestUtils.setField(service, "accessKey", "test-key");
        ReflectionTestUtils.setField(service, "secretKey", "test-secret");
        return service;
    }
}
