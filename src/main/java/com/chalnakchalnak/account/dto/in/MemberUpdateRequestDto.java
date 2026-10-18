package com.chalnakchalnak.account.dto.in;

import com.chalnakchalnak.account.entity.Honor;
import com.chalnakchalnak.account.entity.State;
import com.chalnakchalnak.account.vo.in.MemberUpdateRequestVo;
import lombok.Builder;
import lombok.Getter;

@Getter
public class MemberUpdateRequestDto {

    private String memberUuid;
    private String nickname;
    private String gradeUuid;
    private Honor honor;
    private State state;
    private String profileImageKey;
    private Double point;

    @Builder
    public MemberUpdateRequestDto(String memberUuid,
                                  String nickname,
                                  String gradeUuid,
                                  Honor honor,
                                  State state,
                                  String profileImageKey,
                                  Double point) {
        this.memberUuid = memberUuid;
        this.nickname = nickname;
        this.gradeUuid = gradeUuid;
        this.honor = honor;
        this.state = state;
        this.profileImageKey = profileImageKey;
        this.point = point;
    }

    public static MemberUpdateRequestDto from(MemberUpdateRequestVo memberUpdateRequestVo, String memberUuid) {
        return MemberUpdateRequestDto.builder()
                .memberUuid(memberUuid)
                .nickname(memberUpdateRequestVo.getNickname())
                .profileImageKey(memberUpdateRequestVo.getProfileImageKey())
                .build();
    }
}
