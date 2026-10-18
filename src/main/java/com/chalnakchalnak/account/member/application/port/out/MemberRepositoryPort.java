package com.chalnakchalnak.account.member.application.port.out;

import com.chalnakchalnak.account.member.application.command.UpdateMemberCommand;
import com.chalnakchalnak.account.member.domain.MemberProfile;
import java.util.List;
import java.util.Optional;

public interface MemberRepositoryPort {
  Optional<MemberProfile> findByMemberUuid(String memberUuid);

  List<MemberProfile> findAll();

  boolean existsByNickname(String nickname);

  long updateDynamic(UpdateMemberCommand command);

  void createProfile(
      String memberUuid, String nickname, String gradeUuid, String imageKey, double points);

  void updateProfileImage(String memberUuid, String imageKey);
}
