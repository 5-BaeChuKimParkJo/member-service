package com.chalnakchalnak.member_service.dto.in;

import com.chalnakchalnak.member_service.vo.PresignedUrlRequestVo;
import lombok.Builder;
import lombok.Getter;

@Getter
public class PresignedUrlRequestDto {

    private String fileName;
    private String contentType;

    @Builder
    public PresignedUrlRequestDto(String fileName, String contentType) {
        this.fileName = fileName;
        this.contentType = contentType;
    }

    public static PresignedUrlRequestDto toPresignedUrlRequestDto(PresignedUrlRequestVo presignedUrlRequestVo) {
        return PresignedUrlRequestDto.builder()
                .fileName(presignedUrlRequestVo.getFileName())
                .contentType(presignedUrlRequestVo.getContentType())
                .build();
    }
}
