package com.chalnakchalnak.account.auth.adapter.out.persistence.postgresql.repository;

import com.chalnakchalnak.account.auth.adapter.out.persistence.postgresql.entity.AuthEntity;
import com.chalnakchalnak.account.auth.adapter.out.persistence.postgresql.mapper.AuthEntityMapper;
import com.chalnakchalnak.account.auth.application.command.StoreAuthCommand;
import com.chalnakchalnak.account.auth.application.port.out.AuthRepositoryPort;
import com.chalnakchalnak.account.auth.application.query.MemberIdLookupQuery;
import com.chalnakchalnak.account.auth.application.result.AuthResult;
import com.chalnakchalnak.account.auth.application.result.MemberIdResult;
import com.chalnakchalnak.account.auth.common.exception.BaseException;
import com.chalnakchalnak.account.auth.common.response.AuthErrorCode;
import jakarta.transaction.Transactional;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class AuthJpaAdapter implements AuthRepositoryPort {

  private final AuthJpaRepository authJpaRepository;
  private final AuthEntityMapper authEntityMapper;

  @Override
  public void save(StoreAuthCommand signUpDto) {
    authJpaRepository.save(authEntityMapper.toEntity(signUpDto));
  }

  @Override
  public Boolean existsByMemberId(String memberId) {
    return authJpaRepository.existsByMemberId(memberId);
  }

  //    @Override
  //    public Boolean existsByNickname(String nickname) {
  //        return authJpaRepository.existsByNickname(nickname);
  //    }

  @Override
  public Boolean existsByPhoneNumber(String phoneNumber) {

    return authJpaRepository.existsByPhoneNumber(phoneNumber);
  }

  @Override
  public Optional<AuthResult> findByMemberId(String memberId) {

    return authJpaRepository.findByMemberId(memberId).map(authEntityMapper::toAuthResponseDto);
  }

  @Override
  public Optional<MemberIdResult> findMemberIdByPhoneNumber(MemberIdLookupQuery getMemberIdDto) {

    return authJpaRepository
        .findMemberIdByPhoneNumber(getMemberIdDto.getPhoneNumber())
        .map(authEntityMapper::toGetMemberIdResponseDto);
  }

  @Override
  @Transactional
  public void resetPassword(String phoneNumber, String encryptedPassword) {
    AuthEntity authEntity =
        authJpaRepository
            .findByPhoneNumber(phoneNumber)
            .orElseThrow(() -> new BaseException(AuthErrorCode.USER_NOT_FOUND));

    authEntity.resetPassword(encryptedPassword);
  }
}
