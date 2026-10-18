package com.chalnakchalnak.account.member.adapter.in.web;

import com.chalnakchalnak.account.member.adapter.in.web.request.MemberListRequest;
import com.chalnakchalnak.account.member.adapter.in.web.request.RegisterMemberRequest;
import com.chalnakchalnak.account.member.adapter.in.web.request.UpdateMemberRequest;
import com.chalnakchalnak.account.member.adapter.in.web.response.ChatMemberResponse;
import com.chalnakchalnak.account.member.adapter.in.web.response.MemberResponse;
import com.chalnakchalnak.account.member.application.command.RegisterMemberCommand;
import com.chalnakchalnak.account.member.application.command.UpdateMemberCommand;
import com.chalnakchalnak.account.member.application.port.in.MemberUseCase;
import com.chalnakchalnak.account.member.application.query.MemberListQuery;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

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

  private final MemberUseCase memberService;

  @Operation(summary = "회원 정보 조회(채팅방)")
  @GetMapping("/chatroom/{memberUuid}")
  public ChatMemberResponse getMemberData(@PathVariable String memberUuid) {
    return MemberWebMapper.toResponse(memberService.getChatMember(memberUuid));
  }

  @Operation(summary = "내 정보 조회")
  @GetMapping
  public MemberResponse getMyMemberData(@RequestHeader("X-Member-Uuid") String memberUuid) {
    return MemberWebMapper.toResponse(memberService.getMember(memberUuid));
  }

  @Operation(summary = "회원 단일 조회")
  @GetMapping("/{memberUuid}")
  public MemberResponse getMember(@PathVariable String memberUuid) {
    return MemberWebMapper.toResponse(memberService.getMember(memberUuid));
  }

  @Operation(summary = "회원 내역 조회")
  @PostMapping("/list")
  public List<MemberResponse> getMemberList(
      @RequestBody MemberListRequest memberUuidListRequestVo) {
    return memberService
        .getMemberList(
            MemberListQuery.builder()
                .memberUuidList(memberUuidListRequestVo.getMemberUuidList())
                .build())
        .stream()
        .map(MemberWebMapper::toResponse)
        .toList();
  }

  @Operation(summary = "회원 전체 조회 - 테스트용")
  @GetMapping("/all")
  public List<MemberResponse> getAllMember() {
    return memberService.getAllMemberList().stream().map(MemberWebMapper::toResponse).toList();
  }

  @Operation(summary = "회원 데이터 동적 수정")
  @PutMapping("/update")
  public void updateDynamic(
      @RequestHeader("X-Member-Uuid") String memberUuid,
      @RequestBody UpdateMemberRequest memberUpdateRequestVo) {
    memberService.updateDynamic(
        UpdateMemberCommand.builder()
            .memberUuid(memberUuid)
            .nickname(memberUpdateRequestVo.getNickname())
            .profileImageKey(memberUpdateRequestVo.getProfileImageKey())
            .build());
  }

  @Operation(summary = "회원 가입")
  @PostMapping("/sign-up")
  public void signUp(@RequestBody RegisterMemberRequest signUpRequestVo) {
    memberService.signUp(
        RegisterMemberCommand.builder()
            .memberUuid(signUpRequestVo.getMemberUuid())
            .nickname(signUpRequestVo.getNickname())
            .build());
  }

  @Operation(summary = "닉네임 중복 검사")
  @GetMapping("/exists/nickname/{nickname}")
  public Boolean checkNickname(@PathVariable String nickname) {
    return memberService.existNickname(nickname);
  }
}
