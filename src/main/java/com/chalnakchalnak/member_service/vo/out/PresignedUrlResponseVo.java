package com.chalnakchalnak.member_service.vo.out;

import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
public class PresignedUrlResponseVo {

    private String url;
    private Map<String, String> fields;

    @Builder
    public PresignedUrlResponseVo(String url, Map<String, String> fields) {
        this.url = url;
        this.fields = fields;
    }
}
