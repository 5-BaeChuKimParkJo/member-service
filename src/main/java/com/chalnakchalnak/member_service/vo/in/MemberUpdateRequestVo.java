package com.chalnakchalnak.member_service.vo.in;

import com.chalnakchalnak.member_service.entity.Honor;
import com.chalnakchalnak.member_service.entity.State;
import lombok.Getter;

@Getter
public class MemberUpdateRequestVo {
    private String nickname;
    private String gradeUuid;
    private Honor honor;
    private State state;
    private String profileImageKey;
    private Long point;
}
