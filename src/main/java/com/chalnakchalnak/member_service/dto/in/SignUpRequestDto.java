package com.chalnakchalnak.member_service.dto.in;

import com.chalnakchalnak.member_service.entity.Member;
import com.chalnakchalnak.member_service.entity.State;
import com.chalnakchalnak.member_service.vo.SignUpRequestVo;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@Getter
@ToString
@Builder(toBuilder = true)
public class SignUpRequestDto {

    private String memberUuid;
    private String nickname;

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
                .profileImageUrl(null)
                .point(0L)
                .build();
    }
}
