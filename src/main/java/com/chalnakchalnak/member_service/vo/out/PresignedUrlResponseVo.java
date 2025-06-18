package com.chalnakchalnak.member_service.vo.out;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
public class PresignedUrlResponseVo {

    @Schema(description = "S3 url",  nullable = false)
    private String url;

    @Schema(description = "S3 요청 정보",  nullable = false)
    private Map<String, String> fields;

    @Builder
    public PresignedUrlResponseVo(String url, Map<String, String> fields) {
        this.url = url;
        this.fields = fields;
    }
}
