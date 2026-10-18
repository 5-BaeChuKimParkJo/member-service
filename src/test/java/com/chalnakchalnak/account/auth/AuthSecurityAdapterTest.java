package com.chalnakchalnak.account.auth;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.chalnakchalnak.account.auth.adapter.out.security.AuthSecurityAdapter;
import com.chalnakchalnak.account.auth.adapter.out.security.provider.JwtTokenProvider;
import com.chalnakchalnak.account.auth.application.mapper.AuthMapper;
import com.chalnakchalnak.account.auth.common.exception.BaseException;
import com.chalnakchalnak.account.auth.common.response.AuthErrorCode;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

class AuthSecurityAdapterTest {

  @Test
  void mapsInvalidRefreshTokenToUnauthorizedContract() {
    JwtTokenProvider provider = mock(JwtTokenProvider.class);
    when(provider.extractRefreshMemberUuid("invalid"))
        .thenThrow(new IllegalArgumentException("Expected refresh token"));
    AuthSecurityAdapter adapter =
        new AuthSecurityAdapter(
            mock(AuthenticationManager.class),
            provider,
            mock(PasswordEncoder.class),
            new AuthMapper());

    assertThatThrownBy(() -> adapter.getMemberUuidByRefreshToken("invalid"))
        .isInstanceOf(BaseException.class)
        .extracting("status")
        .isEqualTo(AuthErrorCode.INVALID_REFRESH_TOKEN);
  }
}
