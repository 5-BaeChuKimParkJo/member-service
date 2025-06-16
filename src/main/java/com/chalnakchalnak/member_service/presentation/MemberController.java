package com.chalnakchalnak.member_service.presentation;

import com.chalnakchalnak.member_service.application.MemberService;
import com.chalnakchalnak.member_service.dto.in.MemberUpdateRequestDto;
import com.chalnakchalnak.member_service.dto.in.MemberUuidListDto;
import com.chalnakchalnak.member_service.dto.in.SignUpRequestDto;
import com.chalnakchalnak.member_service.vo.in.MemberUpdateRequestVo;
import com.chalnakchalnak.member_service.vo.out.MemberResponseVo;
import com.chalnakchalnak.member_service.vo.in.MemberUuidListRequestVo;
import com.chalnakchalnak.member_service.vo.in.SignUpRequestVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Member", description = "회원 관련 API")
@RequestMapping("/api/v1/member")
@RestController
@RequiredArgsConstructor
@Slf4j
public class MemberController {

    private final MemberService memberService;

    @Operation(summary = "회원 단일 조회")
    @GetMapping("/{memberUuid}")
    public MemberResponseVo getMember(@PathVariable String memberUuid) {
        return memberService.getMember(memberUuid).toVo();
    }

    @Operation(summary = "회원 내역 조회")
    @PostMapping("/list")
    public List<MemberResponseVo> getMemberList(@RequestBody MemberUuidListRequestVo memberUuidListRequestVo) {
        return memberService.getMemberList(MemberUuidListDto.from(memberUuidListRequestVo))
                .stream()
                .map(memberResponseDto -> memberResponseDto.toVo())
                .toList();
    }

    @Operation(summary = "회원 데이터 동적 수정")
    @PostMapping("/update")
    public void updateDynamic(@RequestBody MemberUpdateRequestVo memberUpdateRequestVo) {
        memberService.updateDynamic(MemberUpdateRequestDto.from(memberUpdateRequestVo));
    }

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
