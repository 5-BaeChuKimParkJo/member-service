package com.chalnakchalnak.member_service.vo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;

@Getter
public class PresignedUrlRequestVo {

    @NotBlank(message = "파일 형식은 필수입니다.")
    @Pattern(
            regexp = "image/(png|jpeg|webp|bmp)",
            message = "지원하지 않는 이미지 형식입니다."
    )
    private String contentType;

    @Builder
    public PresignedUrlRequestVo(String contentType) {
        this.contentType = contentType;
    }
}
