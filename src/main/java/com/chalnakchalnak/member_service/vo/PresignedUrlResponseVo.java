package com.chalnakchalnak.member_service.vo;

import lombok.Builder;
import lombok.Getter;

@Getter
public class PresignedUrlResponseVo {

    private String presignedUrl;
    private String uploadFileUrl;

    @Builder
    public PresignedUrlResponseVo(String presignedUrl, String uploadFileUrl) {
        this.presignedUrl = presignedUrl;
        this.uploadFileUrl = uploadFileUrl;
    }
}
