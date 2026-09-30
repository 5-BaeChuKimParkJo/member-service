package com.chalnakchalnak.member_service.auth;

import com.chalnakchalnak.member_service.auth.adapter.out.security.provider.JwtTokenProvider;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.env.MockEnvironment;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class JwtTokenProviderTest {

    private JwtTokenProvider provider;

    @BeforeEach
    void setUp() {
        MockEnvironment environment = new MockEnvironment()
                .withProperty("JWT.secret-key",
                        "0123456789012345678901234567890123456789012345678901234567890123")
                .withProperty("JWT.token.access-expire-time", "600000")
                .withProperty("JWT.token.refresh-expire-time", "1209600000");
        provider = new JwtTokenProvider(environment);
    }

    @Test
    void issuesAccessAndRefreshTokensWithDistinctIssuers() {
        String access = provider.generateAccessToken("member", "member-uuid");
        String refresh = provider.generateRefreshToken("member", "member-uuid");

        assertThat(provider.extractClaim(access, Claims::getIssuer)).isEqualTo("cn-account");
        assertThat(provider.extractClaim(refresh, Claims::getIssuer)).isEqualTo("cn-account-refresh");
    }

    @Test
    void extractsMemberUuidFromMatchingTokenTypes() {
        String access = provider.generateAccessToken("member", "member-uuid");
        String refresh = provider.generateRefreshToken("member", "member-uuid");

        assertThat(provider.extractAccessMemberUuid(access)).isEqualTo("member-uuid");
        assertThat(provider.extractRefreshMemberUuid(refresh)).isEqualTo("member-uuid");
    }

    @Test
    void rejectsRefreshTokenWhenAccessTokenIsRequired() {
        String refresh = provider.generateRefreshToken("member", "member-uuid");

        assertThatThrownBy(() -> provider.extractAccessMemberUuid(refresh))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("access");
    }

    @Test
    void treatsRefreshExpirationConfigurationAsMilliseconds() {
        String refresh = provider.generateRefreshToken("member", "member-uuid");
        Claims claims = provider.extractClaim(refresh, value -> value);

        long lifetimeMillis = claims.getExpiration().getTime() - claims.getIssuedAt().getTime();

        assertThat(lifetimeMillis).isEqualTo(1_209_600_000L);
    }
}
