package com.chalnakchalnak.member_service.vo.out;

import com.chalnakchalnak.member_service.entity.Honor;
import com.chalnakchalnak.member_service.entity.State;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
public class MemberResponseVo {

    @Schema(description = "회원uuid",  nullable = false)
    private String memberUuid;

    @Schema(description = "회원 닉네임",  nullable = false)
    private String nickname;

    @Schema(description = "회원 등급uuid",  nullable = false)
    private String gradeUuid;

    @Schema(description = "칭호",  nullable = true)
    private Honor honor;

    @Schema(description = "회원 상태",  nullable = false)
    private State state;

    @Schema(description = "회원 프로필 이미지 url",  nullable = true)
    private String profileImageUrl;

    @Schema(description = "포인트",  nullable = true)
    private Long point;

    @Builder
    public MemberResponseVo(String memberUuid,
                            String nickname,
                            String gradeUuid,
                            Honor honor,
                            State state,
                            String profileImageUrl,
                            Long point) {
        this.memberUuid = memberUuid;
        this.nickname = nickname;
        this.gradeUuid = gradeUuid;
        this.honor = honor;
        this.state = state;
        this.profileImageUrl = profileImageUrl;
        this.point = point;
    }
}
