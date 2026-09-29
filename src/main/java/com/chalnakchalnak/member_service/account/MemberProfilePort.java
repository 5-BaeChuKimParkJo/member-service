package com.chalnakchalnak.member_service.account;

public interface MemberProfilePort {

    boolean existsByNickname(String nickname);

    void createProfile(
            String memberUuid,
            String nickname,
            String gradeUuid,
            double initialPoints
    );
}
