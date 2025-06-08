package com.chalnakchalnak.member_service.dto.out;

import com.chalnakchalnak.member_service.vo.PresignedUrlResponseVo;
import lombok.Builder;
import lombok.Getter;

@Getter
public class PresignedUrlResponseDto {

    private String presignedUrl;
    private String uploadFileUrl;

    @Builder
    public PresignedUrlResponseDto(String presignedUrl, String uploadFileUrl) {
        this.presignedUrl = presignedUrl;
        this.uploadFileUrl = uploadFileUrl;
    }

    public PresignedUrlResponseVo toVo() {
        return PresignedUrlResponseVo.builder()
                .presignedUrl(presignedUrl)
                .uploadFileUrl(uploadFileUrl)
                .build();
    }
}
