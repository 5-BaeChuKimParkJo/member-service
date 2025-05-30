package com.chalnakchalnak.member_service.application;

import com.chalnakchalnak.member_service.common.entity.BaseResponseStatus;
import com.chalnakchalnak.member_service.common.exception.BaseException;
import com.chalnakchalnak.member_service.dto.in.SignUpRequestDto;
import com.chalnakchalnak.member_service.infrastructure.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    @Override
    public void signUp(SignUpRequestDto signUpRequestDto) {
        Boolean existsNickname = memberRepository.existsByNickname(signUpRequestDto.getNickname());
        if (existsNickname) {
            throw new BaseException(BaseResponseStatus.DUPLICATE_NICKNAME);
        }
        memberRepository.save(signUpRequestDto.toEntity());
    }

    @Override
    public Boolean existNickname(String nickname) {
        return memberRepository.existsByNickname(nickname);
    }
}
