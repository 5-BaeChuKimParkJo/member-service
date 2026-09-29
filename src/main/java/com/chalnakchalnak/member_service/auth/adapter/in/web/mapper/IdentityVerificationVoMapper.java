package com.chalnakchalnak.member_service.auth.adapter.in.web.mapper;

import com.chalnakchalnak.member_service.auth.adapter.in.web.vo.in.SendVerificationCodeRequestVo;
import com.chalnakchalnak.member_service.auth.adapter.in.web.vo.in.VerifyCodeRequestVo;
import com.chalnakchalnak.member_service.auth.application.port.dto.in.SendVerificationCodeRequestDto;
import com.chalnakchalnak.member_service.auth.application.port.dto.in.VerifyCodeRequestDto;
import org.springframework.stereotype.Component;

@Component
public class IdentityVerificationVoMapper {

    public SendVerificationCodeRequestDto toSendVerificationCodeDto(
            SendVerificationCodeRequestVo sendVerificationCodeRequestVo
    ) {
        return SendVerificationCodeRequestDto.builder()
                .phoneNumber(sendVerificationCodeRequestVo.getPhoneNumber())
                .purpose(sendVerificationCodeRequestVo.getPurpose())
                .build();
    }

    public VerifyCodeRequestDto toVerifyCodeDto(
            VerifyCodeRequestVo verifyCodeRequestVo
    ) {
        return VerifyCodeRequestDto.builder()
                .phoneNumber(verifyCodeRequestVo.getPhoneNumber())
                .verificationCode(verifyCodeRequestVo.getVerificationCode())
                .purpose(verifyCodeRequestVo.getPurpose())
                .build();
    }

}
