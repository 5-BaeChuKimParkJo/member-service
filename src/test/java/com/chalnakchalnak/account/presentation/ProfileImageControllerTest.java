package com.chalnakchalnak.account.presentation;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.S3Object;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import java.io.ByteArrayInputStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class ProfileImageControllerTest {
    private static final String KEY = "member/64c7f981-17f8-4dbf-95c7-fd854d30791f/images/626ef0dd-7c0f-4a67-94b8-a1fb6b439b70.png";

    @Test
    void servesProfileImageBytesFromConfiguredStorage() throws Exception {
        var s3 = mock(AmazonS3.class);
        var object = new S3Object();
        object.setObjectContent(new ByteArrayInputStream(new byte[]{1, 2, 3}));
        object.getObjectMetadata().setContentType("image/png");
        when(s3.getObject("images", KEY)).thenReturn(object);
        var result = new ProfileImageController(s3, "images").read(KEY);
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getHeaders().getContentType().toString()).isEqualTo("image/png");
        assertThat(result.getBody()).containsExactly((byte) 1, (byte) 2, (byte) 3);
    }

    @Test
    void rejectsNonProfileKeysWithoutReadingStorage() {
        var s3 = mock(AmazonS3.class);
        assertThatThrownBy(() -> new ProfileImageController(s3, "images").read("../private.txt"))
                .isInstanceOf(ResponseStatusException.class);
        verifyNoInteractions(s3);
    }
}
