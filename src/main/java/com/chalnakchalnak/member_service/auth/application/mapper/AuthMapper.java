package com.chalnakchalnak.member_service.auth.application.mapper;

import com.chalnakchalnak.member_service.auth.application.port.dto.GetMemberIdDto;
import com.chalnakchalnak.member_service.auth.application.port.dto.SignInDto;
import com.chalnakchalnak.member_service.auth.application.port.dto.SignUpDto;
import com.chalnakchalnak.member_service.auth.application.port.dto.StoreRefreshTokenDto;
import com.chalnakchalnak.member_service.auth.application.port.dto.in.GetMemberIdRequestDto;
import com.chalnakchalnak.member_service.auth.application.port.dto.in.SignUpRequestDto;
import com.chalnakchalnak.member_service.auth.application.port.dto.out.AuthResponseDto;
import com.chalnakchalnak.member_service.auth.application.port.dto.out.SignInResponseDto;
import com.chalnakchalnak.member_service.auth.domain.model.AuthDomain;
import org.springframework.stereotype.Component;

@Component
public class AuthMapper {

    public AuthDomain toAuthDomain(SignUpRequestDto signUpRequestDto, String uuid, String hashedPassword) {
        return AuthDomain.builder()
                .memberUuid(uuid)
                .memberId(signUpRequestDto.getMemberId())
                .password(hashedPassword)
                .nickname(signUpRequestDto.getNickname())
                .phoneNumber(signUpRequestDto.getPhoneNumber())
                .build();
    }

    public AuthDomain toAuthDomain(AuthResponseDto authResponseDto) {
        return AuthDomain.builder()
                .memberUuid(authResponseDto.getMemberUuid())
                .memberId(authResponseDto.getMemberId())
                .password(authResponseDto.getPassword())
                .nickname("")
                .phoneNumber(authResponseDto.getPhoneNumber())
                .build();
    }

    public SignUpDto toSignUpDto(AuthDomain authDomain) {
        return SignUpDto.builder()
                .memberUuid(authDomain.getMemberUuid())
                .memberId(authDomain.getMemberId())
                .password(authDomain.getPassword())
                .phoneNumber(authDomain.getPhoneNumber())
                .build();
    }

    public SignInDto toSignInDto(AuthDomain authDomain) {
        return SignInDto.builder()
                .memberUuid(authDomain.getMemberUuid())
                .memberId(authDomain.getMemberId())
                .password(authDomain.getPassword())
                .build();
    }

    public SignInResponseDto toSignInResponseDto(String memberUuid, String accessToken, String refreshToken) {
        return SignInResponseDto.builder()
                .memberUuid(memberUuid)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public StoreRefreshTokenDto toStoreRefreshTokenDto(String memberUuid, String refreshToken) {
        return StoreRefreshTokenDto.builder()
                .memberUuid(memberUuid)
                .refreshToken(refreshToken)
                .build();
    }

    public GetMemberIdDto toGetMemberIdDto(GetMemberIdRequestDto getMemberIdRequestDto) {
        return GetMemberIdDto.builder()
                .phoneNumber(getMemberIdRequestDto.getPhoneNumber())
                .build();
    }

}
