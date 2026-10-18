package com.chalnakchalnak.account.member.adapter.out.persistence;

import com.chalnakchalnak.account.member.adapter.out.persistence.entity.QMemberEntity;
import com.chalnakchalnak.account.member.application.command.UpdateMemberCommand;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
public class MemberQuerydslAdapter implements MemberUpdateRepository {

  private final JPAQueryFactory queryFactory;

  @Override
  @Transactional
  public long updateDynamic(UpdateMemberCommand memberUpdateRequestDto) {

    QMemberEntity m = QMemberEntity.memberEntity;
    var update = queryFactory.update(m);

    if (memberUpdateRequestDto.getNickname() != null) {
      update.set(m.nickname, memberUpdateRequestDto.getNickname());
    }

    if (memberUpdateRequestDto.getGradeUuid() != null) {
      update.set(m.gradeUuid, memberUpdateRequestDto.getGradeUuid());
    }

    if (memberUpdateRequestDto.getHonor() != null) {
      update.set(m.honor, memberUpdateRequestDto.getHonor());
    }

    if (memberUpdateRequestDto.getState() != null) {
      update.set(m.state, memberUpdateRequestDto.getState());
    }

    if (memberUpdateRequestDto.getProfileImageKey() != null) {
      update.set(m.profileImageKey, memberUpdateRequestDto.getProfileImageKey());
    }

    if (memberUpdateRequestDto.getPoint() != null) {
      update.set(m.point, memberUpdateRequestDto.getPoint());
    }

    return update.where(m.memberUuid.eq(memberUpdateRequestDto.getMemberUuid())).execute();
  }
}
