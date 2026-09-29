package com.chalnakchalnak.member_service.auth.adapter.in.web.presentation;

import com.chalnakchalnak.member_service.auth.adapter.in.web.mapper.IdentityVerificationVoMapper;
import com.chalnakchalnak.member_service.auth.adapter.in.web.vo.in.SendVerificationCodeRequestVo;
import com.chalnakchalnak.member_service.auth.adapter.in.web.vo.in.VerifyCodeRequestVo;
import com.chalnakchalnak.member_service.auth.application.port.in.IdentityVerificationUseCase;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/identity-verification")
@RequiredArgsConstructor
public class IdentityVerificationController {

    private final IdentityVerificationUseCase identityVerificationUseCase;
    private final IdentityVerificationVoMapper identityVerificationVoMapper;

    @Operation(summary = "Send Verification Code API", description = "인증 코드 전송 API", tags = {"identity-verification"})
    @PostMapping("/sms/send")
    public void sendVerificationCode(
            @RequestBody @Valid SendVerificationCodeRequestVo sendVerificationCodeRequestVo
    ) {

        identityVerificationUseCase.sendVerificationCode(
                identityVerificationVoMapper.toSendVerificationCodeDto(sendVerificationCodeRequestVo)
        );
}

    @Operation(summary = "Verify Code API", description = "인증 코드 검증 API", tags = {"identity-verification"})
    @PostMapping("/sms/verify")
    public Boolean verifyCode(
            @RequestBody @Valid VerifyCodeRequestVo verifyCodeRequestVo
    ) {

        return identityVerificationUseCase.verifyCode(
                        identityVerificationVoMapper.toVerifyCodeDto(verifyCodeRequestVo)
                );
    }
}
