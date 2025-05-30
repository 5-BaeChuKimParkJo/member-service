package com.chalnakchalnak.member_service.presentation;

import com.chalnakchalnak.member_service.application.MemberService;
import com.chalnakchalnak.member_service.dto.in.SignUpRequestDto;
import com.chalnakchalnak.member_service.vo.SignUpRequestVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Member", description = "회원 관련 API")
@RequestMapping("/api/v1/member")
@RestController
@RequiredArgsConstructor
@Slf4j
public class MemberController {

    private final MemberService memberService;

    @Operation(summary = "회원 가입")
    @PostMapping("/sign-up")
    public void signUp(@RequestBody SignUpRequestVo signUpRequestVo) {
        memberService.signUp(SignUpRequestDto.from(signUpRequestVo));
    }

    @Operation(summary = "닉네임 중복 검사")
    @GetMapping("/exists/nickname/{nickname}")
    public Boolean checkNickname(@PathVariable String nickname) {
        return memberService.existNickname(nickname);
    }
}
