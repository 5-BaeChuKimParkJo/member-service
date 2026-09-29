package com.chalnakchalnak.member_service.auth.adapter.out.security.provider;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.Objects;
import java.util.function.Function;

@Slf4j
@RequiredArgsConstructor
@Service
public class JwtTokenProvider {

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
                .claim("token_type", "access")
                .claim("role", role)
                .claim("memberUuid", memberUuid)
                .issuedAt(now)
                .expiration(expiration)
                .compact();
    }


    /**
     * 4. 리프레시 토큰 생성
     */
    public String generateRefreshToken(String role, String memberUuid) {
        Date now = new Date();
        Long expireSeconds = Objects.requireNonNull(env.getProperty("JWT.token.refresh-expire-time", Long.class),
                "JWT.token.refresh-expire-time is missing");
        Date expiration = new Date(now.getTime() + expireSeconds * 1000L);

        return Jwts.builder()
                .signWith(getSignKey())
                .claim("token_type", "refresh")
                .claim("role", role)
                .claim("memberUuid", memberUuid)
                .issuedAt(now)
                .expiration(expiration)
                .compact();
    }


    /**
     * 5. memberUuid 추출
     */
    public String extractMemberUuid(String token) {
        try {
            return extractClaim(token, claims -> claims.get("uuid", String.class));
        } catch (ExpiredJwtException e) {
            log.error("만료된 토큰입니다");
            throw new RuntimeException("만료된 토큰입니다");
        }
    }

    /**
     * 6. role 추출
     */
    public String extractRole(String token) {
        try {
            return extractClaim(token, claims -> claims.get("role", String.class));
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
//        byte[] decodedKey = Base64.getDecoder().decode(secret);

        return Keys.hmacShaKeyFor(secret.getBytes());
    }
}
