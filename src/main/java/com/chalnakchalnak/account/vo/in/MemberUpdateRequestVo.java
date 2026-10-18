package com.chalnakchalnak.account.vo.in;

import com.chalnakchalnak.account.entity.Honor;
import com.chalnakchalnak.account.entity.State;
import lombok.Getter;

@Getter
public class MemberUpdateRequestVo {
    private String nickname;
    private String gradeUuid;
    private Honor honor;
    private State state;
    private String profileImageKey;
    private Double point;
}
