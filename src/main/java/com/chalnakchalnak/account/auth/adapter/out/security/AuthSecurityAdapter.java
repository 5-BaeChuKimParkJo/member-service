package com.chalnakchalnak.account.auth.adapter.out.security;

import com.chalnakchalnak.account.auth.adapter.out.security.provider.JwtTokenProvider;
import com.chalnakchalnak.account.auth.application.mapper.AuthMapper;
import com.chalnakchalnak.account.auth.application.port.out.AuthSecurityPort;
import com.chalnakchalnak.account.auth.application.query.AuthenticateAccountQuery;
import com.chalnakchalnak.account.auth.application.result.SignInResult;
import com.chalnakchalnak.account.auth.common.exception.BaseException;
import com.chalnakchalnak.account.auth.common.response.AuthErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class AuthSecurityAdapter implements AuthSecurityPort {

  private final AuthenticationManager authenticationManager;
  private final JwtTokenProvider jwtTokenProvider;
  private final PasswordEncoder passwordEncoder;
  private final AuthMapper authMapper;

  @Override
  public String encryptPassword(String password) {
    return passwordEncoder.encode(password);
  }

  @Override
  @Transactional
  public SignInResult signIn(AuthenticateAccountQuery signInDto, String inputPassword) {
    if (authenticationManager
        .authenticate(
            new UsernamePasswordAuthenticationToken(signInDto.getMemberId(), inputPassword))
        .isAuthenticated()) {

      return generateAllToken(signInDto.getMemberUuid());
    } else {
      throw new RuntimeException("Failed to login");
    }
  }

  @Override
  public String getMemberUuidByRefreshToken(String token) {
    try {
      return jwtTokenProvider.extractRefreshMemberUuid(token);
    } catch (RuntimeException exception) {
      throw new BaseException(AuthErrorCode.INVALID_REFRESH_TOKEN);
    }
  }

  @Override
  public SignInResult generateAllToken(String memberUuid) {

    return authMapper.toSignInResponseDto(
        memberUuid,
        jwtTokenProvider.generateAccessToken("member", memberUuid),
        jwtTokenProvider.generateRefreshToken("member", memberUuid));
  }
}
