package com.chalnakchalnak.account.member.application.service;

import com.chalnakchalnak.account.common.config.MemberCacheNames;
import com.chalnakchalnak.account.common.error.AccountErrorCode;
import com.chalnakchalnak.account.common.exception.BaseException;
import com.chalnakchalnak.account.grade.application.port.out.GradeRepositoryPort;
import com.chalnakchalnak.account.member.application.command.RegisterMemberCommand;
import com.chalnakchalnak.account.member.application.command.UpdateMemberCommand;
import com.chalnakchalnak.account.member.application.port.in.MemberUseCase;
import com.chalnakchalnak.account.member.application.port.out.MemberCachePort;
import com.chalnakchalnak.account.member.application.port.out.MemberRepositoryPort;
import com.chalnakchalnak.account.member.application.query.MemberListQuery;
import com.chalnakchalnak.account.member.application.result.ChatMemberResult;
import com.chalnakchalnak.account.member.application.result.MemberResult;
import com.chalnakchalnak.account.member.domain.MemberProfile;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService implements MemberUseCase {

  @Value("${cloud.aws.s3.bucket}")
  private String bucket;

  @Value("${cloud.aws.region.static}")
  private String region;

  @Value("${cloud.aws.s3.public-base-url:}")
  private String profileImageBaseUrl;

  private final MemberRepositoryPort memberRepository;
  private final GradeRepositoryPort gradeRepository;
  private final MemberCachePort cacheUtil;

  @Cacheable(value = MemberCacheNames.CHAT_MEMBER, key = "#memberUuid")
  @Override
  public ChatMemberResult getChatMember(String memberUuid) {
    MemberProfile member =
        memberRepository
            .findByMemberUuid(memberUuid)
            .orElseThrow(() -> new BaseException(AccountErrorCode.NO_EXISTS_MEMBER));
    return ChatMemberResult.from(member, bucket, region, profileImageBaseUrl);
  }

  @Cacheable(value = MemberCacheNames.MEMBER, key = "#memberUuid")
  @Override
  public MemberResult getMember(String memberUuid) {
    MemberProfile member =
        memberRepository
            .findByMemberUuid(memberUuid)
            .orElseThrow(() -> new BaseException(AccountErrorCode.NO_EXISTS_MEMBER));
    return MemberResult.from(member, bucket, region, profileImageBaseUrl);
  }

  @Cacheable(value = MemberCacheNames.MEMBER_LIST, key = "#memberUuidListDto.memberUuidList")
  @Override
  public List<MemberResult> getMemberList(MemberListQuery memberUuidListDto) {
    List<MemberProfile> memberList =
        memberUuidListDto.getMemberUuidList().stream()
            .map(
                memberUuid ->
                    memberRepository
                        .findByMemberUuid(memberUuid)
                        .orElseThrow(() -> new BaseException(AccountErrorCode.NO_EXISTS_MEMBER)))
            .toList();

    List<MemberResult> memberResponseDtoList = new ArrayList<>();
    for (MemberProfile member : memberList) {
      memberResponseDtoList.add(MemberResult.from(member, bucket, region, profileImageBaseUrl));
    }

    return memberResponseDtoList;
  }

  @Override
  public List<MemberResult> getAllMemberList() {
    return memberRepository.findAll().stream()
        .map(member -> MemberResult.from(member, bucket, region, profileImageBaseUrl))
        .toList();
  }

  @Override
  public void updateDynamic(UpdateMemberCommand memberUpdateRequestDto) {

    MemberProfile member =
        memberRepository
            .findByMemberUuid(memberUpdateRequestDto.getMemberUuid())
            .orElseThrow(() -> new BaseException(AccountErrorCode.NO_EXISTS_MEMBER));

    if (!"".equals(memberUpdateRequestDto.getNickname())) {

      if (member.getNickname().equals(memberUpdateRequestDto.getNickname())) {
        throw new BaseException(AccountErrorCode.ALREADY_USED_NICKNAME);
      }

      if (memberRepository.existsByNickname(memberUpdateRequestDto.getNickname())) {
        throw new BaseException(AccountErrorCode.DUPLICATE_NICKNAME);
      }
    }

    memberRepository.updateDynamic(memberUpdateRequestDto);

    // 관련 캐시 삭제
    cacheUtil.evictMemberCache(MemberCacheNames.MEMBER, memberUpdateRequestDto.getMemberUuid());
    cacheUtil.evictMemberCacheList(
        MemberCacheNames.MEMBER_LIST, memberUpdateRequestDto.getMemberUuid());
    cacheUtil.evictMemberCache(
        MemberCacheNames.CHAT_MEMBER, memberUpdateRequestDto.getMemberUuid());
  }

  @Override
  @Transactional
  public void signUp(RegisterMemberCommand signUpRequestDto) {
    if (memberRepository.findByMemberUuid(signUpRequestDto.getMemberUuid()).isPresent()) {
      throw new BaseException(AccountErrorCode.DUPLICATE_MEMBER);
    }

    Boolean existsNickname = memberRepository.existsByNickname(signUpRequestDto.getNickname());
    if (existsNickname) {
      throw new BaseException(AccountErrorCode.DUPLICATE_NICKNAME);
    }

    String defaultGradeUuid =
        gradeRepository
            .findByOrderNumber(5)
            .orElseThrow(() -> new BaseException(AccountErrorCode.GRADE_API_ERROR))
            .getGradeUuid();
    memberRepository.createProfile(
        signUpRequestDto.getMemberUuid(),
        signUpRequestDto.getNickname(),
        defaultGradeUuid,
        signUpRequestDto.getDefaultProfileImageKey(),
        100.0);
  }

  @Override
  public Boolean existNickname(String nickname) {
    return memberRepository.existsByNickname(nickname);
  }
}
