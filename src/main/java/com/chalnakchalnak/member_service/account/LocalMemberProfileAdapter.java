package com.chalnakchalnak.member_service.account;

import com.chalnakchalnak.member_service.entity.Member;
import com.chalnakchalnak.member_service.entity.State;
import com.chalnakchalnak.member_service.infrastructure.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LocalMemberProfileAdapter implements MemberProfilePort {

    private static final String DEFAULT_PROFILE_IMAGE_KEY =
            "member/temp_member_uuid/images/91a21dc1-0b39-4238-9a4f-5e18de8f48bd.png";

    private final MemberRepository memberRepository;

    @Override
    public boolean existsByNickname(String nickname) {
        return memberRepository.existsByNickname(nickname);
    }

    @Override
    public void createProfile(String memberUuid, String nickname, String gradeUuid, double initialPoints) {
        memberRepository.save(Member.builder()
                .memberUuid(memberUuid)
                .nickname(nickname)
                .gradeUuid(gradeUuid)
                .honor(null)
                .state(State.ACTIVE)
                .profileImageKey(DEFAULT_PROFILE_IMAGE_KEY)
                .point(initialPoints)
                .build());
    }
}
