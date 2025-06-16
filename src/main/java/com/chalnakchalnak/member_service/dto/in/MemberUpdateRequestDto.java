package com.chalnakchalnak.member_service.dto.in;

import com.chalnakchalnak.member_service.entity.Honor;
import com.chalnakchalnak.member_service.entity.State;
import com.chalnakchalnak.member_service.vo.in.MemberUpdateRequestVo;
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
    private Long point;

    @Builder
    public MemberUpdateRequestDto(String memberUuid,
                                  String nickname,
                                  String gradeUuid,
                                  Honor honor,
                                  State state,
                                  String profileImageKey,
                                  Long point) {
        this.memberUuid = memberUuid;
        this.nickname = nickname;
        this.gradeUuid = gradeUuid;
        this.honor = honor;
        this.state = state;
        this.profileImageKey = profileImageKey;
        this.point = point;
    }

    public static MemberUpdateRequestDto from(MemberUpdateRequestVo memberUpdateRequestVo) {
        return MemberUpdateRequestDto.builder()
                .memberUuid(memberUpdateRequestVo.getMemberUuid())
                .nickname(memberUpdateRequestVo.getNickname())
                .gradeUuid(memberUpdateRequestVo.getGradeUuid())
                .honor(memberUpdateRequestVo.getHonor())
                .state(memberUpdateRequestVo.getState())
                .profileImageKey(memberUpdateRequestVo.getProfileImageKey())
                .point(memberUpdateRequestVo.getPoint())
                .build();
    }
}
