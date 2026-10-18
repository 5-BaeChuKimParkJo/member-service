package com.chalnakchalnak.account.auth.application.port.out;

import com.chalnakchalnak.account.auth.application.command.StoreAuthCommand;
import com.chalnakchalnak.account.auth.application.query.MemberIdLookupQuery;
import com.chalnakchalnak.account.auth.application.result.AuthResult;
import com.chalnakchalnak.account.auth.application.result.MemberIdResult;
import java.util.Optional;

public interface AuthRepositoryPort {

  void save(StoreAuthCommand signUpDto);

  Boolean existsByMemberId(String memberId);

  //    Boolean existsByNickname(String nickname);
  Boolean existsByPhoneNumber(String phoneNumber);

  Optional<AuthResult> findByMemberId(String memberId);

  Optional<MemberIdResult> findMemberIdByPhoneNumber(MemberIdLookupQuery getMemberIdDto);

  void resetPassword(String phoneNumber, String encryptedPassword);
}
