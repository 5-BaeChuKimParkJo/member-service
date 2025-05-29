package com.chalnakchalnak.member_service.adapter.in.web.feign.presentation;

import com.chalnakchalnak.member_service.adapter.in.web.mapper.MemberVoMapper;
import com.chalnakchalnak.member_service.adapter.in.web.vo.SignUpVo;
import com.chalnakchalnak.member_service.application.port.in.MemberUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Tag(name = "Member", description = "회원 관련 API")
@RequestMapping("api/v1/member")
@RequiredArgsConstructor
@RestController
public class MemberController {

    private final MemberUseCase memberUseCase;
    private final MemberVoMapper memberVoMapper;

    @Operation(summary = "회원가입")
    @PostMapping("/sign-up")
    public void signUp(@RequestBody SignUpVo signUpVo) {
        memberUseCase.signUp(memberVoMapper.toSignUpRequestDto(signUpVo));
    }

    @Operation(summary = "닉네임 중복 검사")
    @GetMapping("/exists/nickname/{nickname}")
    public Boolean checkNickname(@PathVariable String nickname) {
        return memberUseCase.checkNickname(nickname);
    }
}
