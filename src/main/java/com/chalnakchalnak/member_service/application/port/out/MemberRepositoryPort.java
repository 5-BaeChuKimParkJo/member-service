package com.chalnakchalnak.member_service.application.port.out;

import com.chalnakchalnak.member_service.adapter.out.persistence.mysql.entity.MemberEntity;
import com.chalnakchalnak.member_service.application.port.in.dto.SignUpRequestDto;

public interface MemberRepositoryPort {

    void save(SignUpRequestDto signUpRequestDto);

    Boolean existsByNickname(String nickname);
}
