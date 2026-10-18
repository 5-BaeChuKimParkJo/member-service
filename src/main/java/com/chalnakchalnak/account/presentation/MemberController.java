package com.chalnakchalnak.account.presentation;

import com.chalnakchalnak.account.application.MemberService;
import com.chalnakchalnak.account.dto.in.MemberUpdateRequestDto;
import com.chalnakchalnak.account.dto.in.MemberUuidListDto;
import com.chalnakchalnak.account.dto.in.SignUpRequestDto;
import com.chalnakchalnak.account.vo.in.MemberUpdateRequestVo;
import com.chalnakchalnak.account.vo.out.ChatroomMemberResponseVo;
import com.chalnakchalnak.account.vo.out.MemberResponseVo;
import com.chalnakchalnak.account.vo.in.MemberUuidListRequestVo;
import com.chalnakchalnak.account.vo.in.SignUpRequestVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Member", description = "회원 관련 API")
@RequestMapping({
        "/api/v1/member",
        "/member-service/api/v1/member",
        "/account-service/api/v1/member"
})
@RestController
@RequiredArgsConstructor
@Slf4j
public class MemberController {

    private final MemberService memberService;

    @Operation(summary = "회원 정보 조회(채팅방)")
    @GetMapping("/chatroom/{memberUuid}")
    public ChatroomMemberResponseVo getMemberData(@PathVariable String memberUuid) {
        return memberService.getChatMember(memberUuid).toVo();
    }

    @Operation(summary = "내 정보 조회")
    @GetMapping
    public MemberResponseVo getMyMemberData(@RequestHeader("X-Member-Uuid") String memberUuid) {
        return memberService.getMember(memberUuid).toVo();
    }

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

    @Operation(summary = "회원 전체 조회 - 테스트용")
    @GetMapping("/all")
    public List<MemberResponseVo> getAllMember() {
        return memberService.getAllMemberList()
                .stream()
                .map(memberResponseDto -> memberResponseDto.toVo())
                .toList();
    }

    @Operation(summary = "회원 데이터 동적 수정")
    @PutMapping("/update")
    public void updateDynamic(@RequestHeader("X-Member-Uuid") String memberUuid,
                              @RequestBody MemberUpdateRequestVo memberUpdateRequestVo) {
        memberService.updateDynamic(MemberUpdateRequestDto.from(memberUpdateRequestVo, memberUuid));
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
