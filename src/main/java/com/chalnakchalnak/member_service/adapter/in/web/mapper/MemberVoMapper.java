package com.chalnakchalnak.member_service.adapter.in.web.mapper;

import com.chalnakchalnak.member_service.adapter.in.web.vo.SignUpVo;
import com.chalnakchalnak.member_service.application.port.in.dto.SignUpRequestDto;
import org.springframework.stereotype.Component;

@Component
public class MemberVoMapper {

//    private String memberUuid;
//    private String nickName;
//    private String gradeName;
//    private String honor_name;
//    private State state;
//    private String profileImageUrl;

    public SignUpRequestDto toSignUpRequestDto(SignUpVo signUpVo) {
        return SignUpRequestDto.builder()
                .memberUuid(signUpVo.getMemberUuid())
                .nickname(signUpVo.getNickname())
                .build();
    }
}
