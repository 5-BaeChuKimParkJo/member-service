package com.chalnakchalnak.member_service.dto.in;

import com.chalnakchalnak.member_service.entity.Member;
import com.chalnakchalnak.member_service.entity.State;
import com.chalnakchalnak.member_service.vo.in.SignUpRequestVo;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@Getter
@ToString
public class SignUpRequestDto {

    private String memberUuid;
    private String nickname;
    private String defaultProfileImageKey = "member/temp_member_uuid/images/91a21dc1-0b39-4238-9a4f-5e18de8f48bd.png";

    @Builder
    public SignUpRequestDto(String memberUuid,
                            String nickname) {
        this.memberUuid = memberUuid;
        this.nickname = nickname;
    }

    public static SignUpRequestDto from(SignUpRequestVo signUpRequestVo) {
        return SignUpRequestDto.builder()
                .memberUuid(signUpRequestVo.getMemberUuid())
                .nickname(signUpRequestVo.getNickname())
                .build();
    }

    public Member toEntity(String gradeUuid) {
        return Member.builder()
                .memberUuid(this.memberUuid)
                .nickname(this.nickname)
                .gradeUuid(gradeUuid)
                .honor(null)
                .state(State.ACTIVE)
                .profileImageKey(null)
                .point(100.0)
                .profileImageKey(defaultProfileImageKey)
                .build();
    }
}
