package com.chalnakchalnak.member_service.auth.application.port.out;

import com.chalnakchalnak.member_service.auth.application.port.dto.GetMemberIdDto;
import com.chalnakchalnak.member_service.auth.application.port.dto.SignUpDto;
import com.chalnakchalnak.member_service.auth.application.port.dto.out.AuthResponseDto;
import com.chalnakchalnak.member_service.auth.application.port.dto.out.GetMemberIdResponseDto;

import java.util.Optional;

public interface AuthRepositoryPort {

    void save(SignUpDto signUpDto);
    Boolean existsByMemberId(String memberId);
//    Boolean existsByNickname(String nickname);
    Boolean existsByPhoneNumber(String phoneNumber);
    Optional<AuthResponseDto> findByMemberId(String memberId);
    Optional<GetMemberIdResponseDto> findMemberIdByPhoneNumber (GetMemberIdDto getMemberIdDto);
    void resetPassword(String phoneNumber, String encryptedPassword);

}
