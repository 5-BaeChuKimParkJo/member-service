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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Member", description = "회원 관련 API")
@RequestMapping("/api/v1/member")
@RestController
@RequiredArgsConstructor
@Slf4j
public class MemberController {

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.region.static}")
    private String region;

    private final MemberService memberService;

    @Operation(summary = "내 정보 조회")
    @GetMapping
    public MemberResponseVo getMyMemberData(@RequestHeader("memberUuid") String memberUuid) {
        return memberService.getMember(memberUuid).toVo(bucket, region);
    }

    @Operation(summary = "회원 단일 조회")
    @GetMapping("/{memberUuid}")
    public MemberResponseVo getMember(@PathVariable String memberUuid) {
        return memberService.getMember(memberUuid).toVo(bucket, region);
    }

    @Operation(summary = "회원 내역 조회")
    @PostMapping("/list")
    public List<MemberResponseVo> getMemberList(@RequestBody MemberUuidListRequestVo memberUuidListRequestVo) {
        return memberService.getMemberList(MemberUuidListDto.from(memberUuidListRequestVo))
                .stream()
                .map(memberResponseDto -> memberResponseDto.toVo(bucket, region))
                .toList();
    }

    @Operation(summary = "회원 전체 조회 - 테스트용")
    @GetMapping("/all")
    public List<MemberResponseVo> getAllMember() {
        return memberService.getAllMemberList()
                .stream()
                .map(memberResponseDto -> memberResponseDto.toVo(bucket, region))
                .toList();
    }

    @Operation(summary = "회원 데이터 동적 수정")
    @PostMapping("/update")
    public void updateDynamic(@RequestHeader("memberUuid") String memberUuid,
                              @RequestBody MemberUpdateRequestVo memberUpdateRequestVo) {
        memberService.updateDynamic(MemberUpdateRequestDto.from(memberUpdateRequestVo, memberUuid));
    }

    @Operation(summary = "회원 삭제")
    @DeleteMapping("/{memberUuid}")
    public void deleteMember(String memberUuid) {
        memberService.deleteMember(memberUuid);
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
