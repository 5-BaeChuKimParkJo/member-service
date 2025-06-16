package com.chalnakchalnak.member_service.dto.in;

import com.chalnakchalnak.member_service.vo.in.PresignedUrlRequestVo;
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

    public static PresignedUrlRequestDto toPresignedUrlRequestDto(PresignedUrlRequestVo presignedUrlRequestVo,
                                                                  String memberUuid) {
        String ext = presignedUrlRequestVo.getContentType()
                .substring(presignedUrlRequestVo.getContentType().indexOf("/") + 1);

        return PresignedUrlRequestDto.builder()
                .key("member/" + memberUuid + "/" + "images/" + UUID.randomUUID() + "." + ext)
                .contentType(presignedUrlRequestVo.getContentType())
                .build();
    }
}
