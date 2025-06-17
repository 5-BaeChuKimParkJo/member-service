package com.chalnakchalnak.member_service.vo.out;

import com.chalnakchalnak.member_service.entity.Honor;
import com.chalnakchalnak.member_service.entity.State;
import lombok.Builder;
import lombok.Getter;

@Getter
public class MemberResponseVo {

    private String memberUuid;
    private String nickname;
    private String gradeUuid;
    private Honor honor;
    private State state;
    private String profileImageUrl;
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
