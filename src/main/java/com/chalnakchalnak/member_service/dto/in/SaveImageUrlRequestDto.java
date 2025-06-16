package com.chalnakchalnak.member_service.dto.in;

import com.chalnakchalnak.member_service.vo.in.SaveImageUrlRequestVo;
import lombok.Builder;
import lombok.Getter;

@Getter
public class SaveImageUrlRequestDto {

    private String profileImageKey;
    private String memberUuid;

    @Builder
    public SaveImageUrlRequestDto(String profileImageKey,
                                  String memberUuid) {
        this.profileImageKey = profileImageKey;
        this.memberUuid = memberUuid;
    }

    public static SaveImageUrlRequestDto from(SaveImageUrlRequestVo saveImageUrlRequestVo, String memberUuid) {
        return SaveImageUrlRequestDto.builder()
                .profileImageKey(saveImageUrlRequestVo.getProfileImageKey())
                .memberUuid(memberUuid)
                .build();
    }
}
