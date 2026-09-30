package com.chalnakchalnak.member_service.auth.adapter.out.security.provider;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;
import java.util.Objects;
import java.util.function.Function;

@Slf4j
@RequiredArgsConstructor
@Service
public class JwtTokenProvider {

    private static final String ACCESS_ISSUER = "cn-account";
    private static final String REFRESH_ISSUER = "cn-account-refresh";
    private static final String TOKEN_TYPE_CLAIM = "token_type";
    private static final String MEMBER_UUID_CLAIM = "memberUuid";
    private static final String ROLE_CLAIM = "role";
    private static final String ACCESS_TOKEN_TYPE = "access";
    private static final String REFRESH_TOKEN_TYPE = "refresh";

    private final Environment env;

    /**
     * 1. Claims에서 원하는 claim 값 추출
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * 2. 토큰에서 모든 claims 추출
     */
    private Claims extractAllClaims(String token) {
        try {
            return Jwts
                    .parser()
                    .verifyWith((SecretKey) getSignKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (ExpiredJwtException e) {
            log.error("만료된 토큰입니다");
            throw new RuntimeException("만료된 토큰입니다");
        } catch (UnsupportedJwtException e) {
            log.error("지원되지 않는 유형의 토큰입니다");
            throw new RuntimeException("지원되지 않는 유형의 토큰입니다");
        } catch (MalformedJwtException | IllegalArgumentException e) {
            log.error("잘못된 토큰입니다");
            throw new RuntimeException("잘못된 토큰입니다");
        } catch (io.jsonwebtoken.security.SignatureException e) {
            log.error("SecretKey가 일치하지 않습니다");
            throw new RuntimeException("SecretKey가 일치하지 않습니다");
        }
    }

    /**
     * 3. 액세스 토큰 생성
     */
    public String generateAccessToken(String role, String memberUuid) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() +
                Objects.requireNonNull(env.getProperty("JWT.token.access-expire-time", Long.class),
                        "JWT.token.access-expire-time is missing"));

        return Jwts.builder()
                .signWith(getSignKey())
                .issuer(ACCESS_ISSUER)
                .claim(TOKEN_TYPE_CLAIM, ACCESS_TOKEN_TYPE)
                .claim(ROLE_CLAIM, role)
                .claim(MEMBER_UUID_CLAIM, memberUuid)
                .issuedAt(now)
                .expiration(expiration)
                .compact();
    }


    /**
     * 4. 리프레시 토큰 생성
     */
    public String generateRefreshToken(String role, String memberUuid) {
        Date now = new Date();
        Long expireMillis = Objects.requireNonNull(env.getProperty("JWT.token.refresh-expire-time", Long.class),
                "JWT.token.refresh-expire-time is missing");
        Date expiration = new Date(now.getTime() + expireMillis);

        return Jwts.builder()
                .signWith(getSignKey())
                .issuer(REFRESH_ISSUER)
                .claim(TOKEN_TYPE_CLAIM, REFRESH_TOKEN_TYPE)
                .claim(ROLE_CLAIM, role)
                .claim(MEMBER_UUID_CLAIM, memberUuid)
                .issuedAt(now)
                .expiration(expiration)
                .compact();
    }


    /**
     * 5. memberUuid 추출
     */
    public String extractAccessMemberUuid(String token) {
        return extractMemberUuid(token, ACCESS_ISSUER, ACCESS_TOKEN_TYPE);
    }

    public String extractRefreshMemberUuid(String token) {
        return extractMemberUuid(token, REFRESH_ISSUER, REFRESH_TOKEN_TYPE);
    }

    public String extractMemberUuid(String token) {
        return extractAccessMemberUuid(token);
    }

    private String extractMemberUuid(String token, String expectedIssuer, String expectedType) {
        Claims claims = extractAllClaims(token);
        if (!expectedIssuer.equals(claims.getIssuer())
                || !expectedType.equals(claims.get(TOKEN_TYPE_CLAIM, String.class))) {
            throw new IllegalArgumentException("Expected " + expectedType + " token");
        }

        return claims.get(MEMBER_UUID_CLAIM, String.class);
    }

    /**
     * 6. role 추출
     */
    public String extractRole(String token) {
        try {
            return extractClaim(token, claims -> claims.get(ROLE_CLAIM, String.class));
        } catch (ExpiredJwtException e) {
            log.error("만료된 토큰입니다");
            throw new RuntimeException("만료된 토큰입니다");
        }
    }

    /**
     * 서명 키 생성
     */
    public Key getSignKey() {
        String secret = Objects.requireNonNull(env.getProperty("JWT.secret-key"));
        return Keys.hmacShaKeyFor(secret.getBytes());
    }
}
