package com.chalnakchalnak.account.auth.application.port.out;

import com.chalnakchalnak.account.auth.application.query.AuthenticateAccountQuery;
import com.chalnakchalnak.account.auth.application.result.SignInResult;

public interface AuthSecurityPort {

  String encryptPassword(String password);

  SignInResult signIn(AuthenticateAccountQuery signInDto, String inputPassword);

  String getMemberUuidByRefreshToken(String token);

  SignInResult generateAllToken(String memberUuid);
}
