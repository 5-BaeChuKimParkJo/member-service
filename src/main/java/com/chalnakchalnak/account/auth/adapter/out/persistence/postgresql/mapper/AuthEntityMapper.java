package com.chalnakchalnak.account.auth.adapter.out.persistence.postgresql.mapper;

import com.chalnakchalnak.account.auth.adapter.out.persistence.postgresql.entity.AuthEntity;
import com.chalnakchalnak.account.auth.application.command.StoreAuthCommand;
import com.chalnakchalnak.account.auth.application.result.AuthResult;
import com.chalnakchalnak.account.auth.application.result.MemberIdResult;
import org.springframework.stereotype.Component;

@Component
public class AuthEntityMapper {

  public AuthEntity toEntity(StoreAuthCommand signUpDto) {
    return AuthEntity.builder()
        .memberUuid(signUpDto.getMemberUuid())
        .memberId(signUpDto.getMemberId())
        .password(signUpDto.getPassword())
        .phoneNumber(signUpDto.getPhoneNumber())
        .build();
  }

  public AuthResult toAuthResponseDto(AuthEntity authEntity) {
    return AuthResult.builder()
        .memberUuid(authEntity.getMemberUuid())
        .memberId(authEntity.getMemberId())
        .password(authEntity.getPassword())
        .phoneNumber(authEntity.getPhoneNumber())
        .createdAt(authEntity.getCreatedAt())
        .updatedAt(authEntity.getUpdatedAt())
        .build();
  }

  public MemberIdResult toGetMemberIdResponseDto(AuthEntity authEntity) {
    return MemberIdResult.builder().memberId(authEntity.getMemberId()).build();
  }
}
