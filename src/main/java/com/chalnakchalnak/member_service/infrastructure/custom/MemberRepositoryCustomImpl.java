package com.chalnakchalnak.member_service.infrastructure.custom;

import com.chalnakchalnak.member_service.dto.in.MemberUpdateRequestDto;
import com.chalnakchalnak.member_service.entity.Member;
import com.chalnakchalnak.member_service.entity.QMember;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MemberRepositoryCustomImpl implements MemberRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    @Transactional
    public long updateDynamic(MemberUpdateRequestDto memberUpdateRequestDto) {

        QMember m = QMember.member;
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

        return update
                .where(m.memberUuid.eq(memberUpdateRequestDto.getMemberUuid()))
                .execute();
    }
}
