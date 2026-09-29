package com.chalnakchalnak.member_service.auth.adapter.out.security;

import com.chalnakchalnak.member_service.auth.adapter.out.security.provider.JwtTokenProvider;
import com.chalnakchalnak.member_service.auth.application.mapper.AuthMapper;
import com.chalnakchalnak.member_service.auth.application.port.dto.SignInDto;
import com.chalnakchalnak.member_service.auth.application.port.dto.out.SignInResponseDto;
import com.chalnakchalnak.member_service.auth.application.port.out.AuthSecurityPort;
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
    public SignInResponseDto signIn(SignInDto signInDto, String inputPassword) {
        if(authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        signInDto.getMemberId(),
                        inputPassword
                )
        ).isAuthenticated()) {

            return generateAllToken(signInDto.getMemberUuid());
        } else {
            throw new RuntimeException("Failed to login");
        }
    }

    @Override
    public String getMemberUuidByToken(String token) {
        return jwtTokenProvider.extractMemberUuid(token);
    }

    @Override
    public SignInResponseDto generateAllToken(String memberUuid) {

        return authMapper.toSignInResponseDto(
                memberUuid,
                jwtTokenProvider.generateAccessToken("member", memberUuid),
                jwtTokenProvider.generateRefreshToken("member", memberUuid)
        );
    }
}
