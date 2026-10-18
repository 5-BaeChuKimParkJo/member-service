package com.chalnakchalnak.account.dto.out;

import com.chalnakchalnak.account.vo.out.PresignedUrlResponseVo;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
public class PresignedUrlResponseDto {

    private String url;
    private Map<String, String> fields;

    @Builder
    public PresignedUrlResponseDto(String url, Map<String, String> fields) {
        this.url = url;
        this.fields = fields;
    }

    public PresignedUrlResponseVo toVo() {
        return PresignedUrlResponseVo.builder()
                .url(url)
                .fields(fields)
                .build();
    }
}
