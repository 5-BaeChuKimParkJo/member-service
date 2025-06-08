package com.chalnakchalnak.member_service.dto.in;

import com.chalnakchalnak.member_service.vo.SaveImageUrlRequestVo;
import lombok.Builder;
import lombok.Getter;

@Getter
public class SaveImageUrlRequestDto {

    private String uploadFileUrl;
    private String memberUuid;

    @Builder
    public SaveImageUrlRequestDto(String uploadFileUrl,
                                  String memberUuid) {
        this.uploadFileUrl = uploadFileUrl;
        this.memberUuid = memberUuid;
    }

    public static SaveImageUrlRequestDto from(SaveImageUrlRequestVo saveImageUrlRequestVo, String memberUuid) {
        return SaveImageUrlRequestDto.builder()
                .uploadFileUrl(saveImageUrlRequestVo.getUploadFileUrl())
                .memberUuid(memberUuid)
                .build();
    }
}
