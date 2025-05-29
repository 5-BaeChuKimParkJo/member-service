package com.chalnakchalnak.member_service.application.service;

import com.chalnakchalnak.member_service.adapter.out.persistence.mysql.repository.MemberRepository;
import com.chalnakchalnak.member_service.application.mapper.feign.MemberMapper;
import com.chalnakchalnak.member_service.application.port.in.MemberUseCase;
import com.chalnakchalnak.member_service.application.port.in.dto.SignUpRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService implements MemberUseCase {

    private final MemberRepository memberRepository;
//    private final MemberMapper memberMapper;

    @Override
    public void signUp(SignUpRequestDto signUpRequestDto) {
        memberRepository.save(signUpRequestDto);
    }

    @Override
    public Boolean checkNickname(String nickname) {
        return memberRepository.existsByNickname(nickname);
    }
}
