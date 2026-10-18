package com.chalnakchalnak.account.registration.application.port.out;

public interface MemberProfilePort {

  boolean existsByNickname(String nickname);

  void createProfile(String memberUuid, String nickname, String gradeUuid, double initialPoints);
}
