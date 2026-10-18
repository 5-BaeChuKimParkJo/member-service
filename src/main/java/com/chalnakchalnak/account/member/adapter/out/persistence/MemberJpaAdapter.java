package com.chalnakchalnak.account.member.adapter.out.persistence;

import com.chalnakchalnak.account.common.error.AccountErrorCode;
import com.chalnakchalnak.account.common.exception.BaseException;
import com.chalnakchalnak.account.member.adapter.out.persistence.entity.MemberEntity;
import com.chalnakchalnak.account.member.application.command.UpdateMemberCommand;
import com.chalnakchalnak.account.member.application.port.out.MemberRepositoryPort;
import com.chalnakchalnak.account.member.domain.MemberProfile;
import com.chalnakchalnak.account.member.domain.State;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
public class MemberJpaAdapter implements MemberRepositoryPort {
  private final MemberJpaRepository repository;
  private final MemberUpdateRepository updates;

  @Override
  public Optional<MemberProfile> findByMemberUuid(String uuid) {
    return repository.findByMemberUuid(uuid).map(this::toProfile);
  }

  @Override
  public List<MemberProfile> findAll() {
    return repository.findAll().stream().map(this::toProfile).toList();
  }

  @Override
  public boolean existsByNickname(String nickname) {
    return repository.existsByNickname(nickname);
  }

  @Override
  public long updateDynamic(UpdateMemberCommand command) {
    return updates.updateDynamic(command);
  }

  @Override
  public void createProfile(
      String uuid, String nickname, String gradeUuid, String imageKey, double points) {
    repository.save(
        MemberEntity.builder()
            .memberUuid(uuid)
            .nickname(nickname)
            .gradeUuid(gradeUuid)
            .honor(null)
            .state(State.ACTIVE)
            .profileImageKey(imageKey)
            .point(points)
            .build());
  }

  @Override
  @Transactional
  public void updateProfileImage(String uuid, String imageKey) {
    repository
        .findByMemberUuid(uuid)
        .orElseThrow(() -> new BaseException(AccountErrorCode.NO_EXISTS_MEMBER))
        .setProfileImageKey(imageKey);
  }

  private MemberProfile toProfile(MemberEntity member) {
    return MemberProfile.builder()
        .memberUuid(member.getMemberUuid())
        .nickname(member.getNickname())
        .gradeUuid(member.getGradeUuid())
        .honor(member.getHonor())
        .state(member.getState())
        .profileImageKey(member.getProfileImageKey())
        .point(member.getPoint())
        .build();
  }
}
