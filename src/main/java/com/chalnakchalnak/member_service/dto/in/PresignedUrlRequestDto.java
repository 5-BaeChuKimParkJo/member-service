package com.chalnakchalnak.member_service.dto.in;

import com.chalnakchalnak.member_service.vo.PresignedUrlRequestVo;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
public class PresignedUrlRequestDto {

    private String key;
    private String contentType;

    @Builder
    public PresignedUrlRequestDto(String key, String contentType) {
        this.key = key;
        this.contentType = contentType;
    }

    public static PresignedUrlRequestDto toPresignedUrlRequestDto(PresignedUrlRequestVo presignedUrlRequestVo) {
        return PresignedUrlRequestDto.builder()
                .key(UUID.randomUUID().toString())
                .contentType(presignedUrlRequestVo.getContentType())
                .build();
    }
}
