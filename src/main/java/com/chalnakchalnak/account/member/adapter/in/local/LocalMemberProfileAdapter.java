package com.chalnakchalnak.account.member.adapter.in.local;

import com.chalnakchalnak.account.member.application.port.out.MemberRepositoryPort;
import com.chalnakchalnak.account.registration.application.port.out.MemberProfilePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LocalMemberProfileAdapter implements MemberProfilePort {

  private static final String DEFAULT_PROFILE_IMAGE_KEY =
      "member/temp_member_uuid/images/91a21dc1-0b39-4238-9a4f-5e18de8f48bd.png";

  private final MemberRepositoryPort memberRepository;

  @Override
  public boolean existsByNickname(String nickname) {
    return memberRepository.existsByNickname(nickname);
  }

  @Override
  public void createProfile(
      String memberUuid, String nickname, String gradeUuid, double initialPoints) {
    memberRepository.createProfile(
        memberUuid, nickname, gradeUuid, DEFAULT_PROFILE_IMAGE_KEY, initialPoints);
  }
}
