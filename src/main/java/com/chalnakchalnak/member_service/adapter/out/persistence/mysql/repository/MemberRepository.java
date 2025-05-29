package com.chalnakchalnak.member_service.adapter.out.persistence.mysql.repository;

import com.chalnakchalnak.member_service.adapter.out.persistence.mysql.entity.MemberEntity;
import com.chalnakchalnak.member_service.adapter.out.persistence.mysql.mapper.MemberEntityMapper;
import com.chalnakchalnak.member_service.application.port.in.dto.SignUpRequestDto;
import com.chalnakchalnak.member_service.application.port.out.MemberRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MemberRepository implements MemberRepositoryPort {

    private final MemberJpaRepository memberJpaRepository;
    private final MemberEntityMapper memberEntityMapper;

    @Override
    public void save(SignUpRequestDto signUpRequestDto) {
        memberJpaRepository.save(memberEntityMapper.toEntity(signUpRequestDto));
    }

    @Override
    public Boolean existsByNickname(String nickname) {
        return memberJpaRepository.existsByNickname(nickname);
    }
}
