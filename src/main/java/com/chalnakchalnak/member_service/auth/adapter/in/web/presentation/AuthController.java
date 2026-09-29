package com.chalnakchalnak.member_service.auth.adapter.in.web.presentation;

import com.chalnakchalnak.member_service.auth.adapter.in.web.mapper.AuthVoMapper;
import com.chalnakchalnak.member_service.auth.adapter.in.web.vo.in.*;
import com.chalnakchalnak.member_service.auth.adapter.in.web.vo.out.GetMemberIdResponseVo;
import com.chalnakchalnak.member_service.auth.adapter.in.web.vo.out.SignInResponseVo;
import com.chalnakchalnak.member_service.auth.application.port.dto.out.GetMemberIdResponseDto;
import com.chalnakchalnak.member_service.auth.application.port.in.AuthUseCase;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthUseCase authUseCase;
    private final AuthVoMapper authVoMapper;

    @Operation(summary = "Sign Up API", description = "회원가입", tags = {"auth"})
    @PostMapping("/sign-up")
    public void signUp(@RequestBody @Valid SignUpRequestVo signUpRequestVo) {

        authUseCase.signUp(authVoMapper.toSignUpRequestDto(signUpRequestVo));
    }

    @Operation(summary = "Check Member ID API", description = "회원 아이디 중복 확인", tags = {"auth"})
    @GetMapping("/exists/member-id")
    public Boolean existsMemberId(
            @ModelAttribute @Valid ExistsMemberIdRequestVo existsMemberIdRequestVo
    ) {

        return authUseCase.existsMemberId(
                authVoMapper.toExistsMemberIdRequestDto(existsMemberIdRequestVo)
        );
    }

    @Operation(summary = "Check Nickname API", description = "닉네임 중복 확인", tags = {"auth"})
    @GetMapping("/exists/nickname")
    public Boolean existsNickname(
            @ModelAttribute @Valid ExistsNicknameRequestVo existsNicknameRequestVo
    ) {

        return authUseCase.existsNickname(
                authVoMapper.toExistsNicknameRequestDto(existsNicknameRequestVo)
        );
    }

    @Operation(summary = "Check PhoneNumber API", description = "전화번호 중복 확인", tags = {"auth"})
    @GetMapping("/exists/phone-number")
    public Boolean existsPhoneNumber(
            @ModelAttribute @Valid ExistsPhoneNumberRequestVo existsMemberIdRequestVo
    ) {

        return authUseCase.existsPhoneNumber(
                authVoMapper.toExistsPhoneNumberRequestDto(existsMemberIdRequestVo)
        );
    }

    @Operation(summary = "Sign In API", description = "로그인", tags = {"auth"})
    @PostMapping("/sign-in")
    public SignInResponseVo signIn(
            @RequestBody @Valid SignInRequestVo signInRequestVo
            ) {

        return authVoMapper.toSignInResponseVo(
                authUseCase.signIn(
                        authVoMapper.toSignInRequestDto(signInRequestVo)
                )
        );
    }

    @Operation(summary = "Reissue Token API", description = "토큰 재발급", tags = {"auth"})
    @GetMapping("/reissue")
    public SignInResponseVo reissueAllToken(
            @RequestHeader("Authorization") String refreshToken
    ) {

        return authVoMapper.toSignInResponseVo(authUseCase.reissueAllToken(
                        authVoMapper.toReissueAllTokenRequestDto(
                                refreshToken.substring(7))
                )
        );
    }

    @Operation(summary = "Sign Out API", description = "로그아웃", tags = {"auth"})
    @GetMapping("/sign-out")
    public void signOut(
            @RequestHeader("Authorization") String refreshToken
    ) {

        authUseCase.signOut(
                authVoMapper.toSignOutDto(refreshToken.substring(7))
        );
    }

    @Operation(summary = "Find Member ID API", description = "회원 아이디 찾기", tags = {"auth"})
    @GetMapping("/member-id")
    public GetMemberIdResponseVo findMemberId(
            @ModelAttribute @Valid GetMemberIdRequestVo getMemberIdRequestVo
    ) {

        return authVoMapper.toGetMemberIdResponseVo(
                authUseCase.getMemberId(
                        authVoMapper.toGetMemberIdRequestDto(getMemberIdRequestVo)
                )
        );
    }

    @Operation(summary = "Reest Password API", description = "비밀번호 재설정", tags = {"auth"})
    @PatchMapping("/password")
    public void resetPassword(
            @RequestBody @Valid ResetPasswordRequestVo resetPasswordRequestVo
    ) {

        authUseCase.resetPassword(
                authVoMapper.toResetPasswordRequestDto(resetPasswordRequestVo)
        );
    }

}
