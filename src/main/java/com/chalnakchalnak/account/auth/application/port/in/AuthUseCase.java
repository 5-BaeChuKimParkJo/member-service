package com.chalnakchalnak.account.auth.application.port.in;

import com.chalnakchalnak.account.auth.application.command.ReissueAllTokenCommand;
import com.chalnakchalnak.account.auth.application.command.ResetPasswordCommand;
import com.chalnakchalnak.account.auth.application.command.SignInCommand;
import com.chalnakchalnak.account.auth.application.command.SignOutCommand;
import com.chalnakchalnak.account.auth.application.command.SignUpCommand;
import com.chalnakchalnak.account.auth.application.query.ExistsMemberIdQuery;
import com.chalnakchalnak.account.auth.application.query.ExistsNicknameQuery;
import com.chalnakchalnak.account.auth.application.query.ExistsPhoneNumberQuery;
import com.chalnakchalnak.account.auth.application.query.GetMemberIdQuery;
import com.chalnakchalnak.account.auth.application.result.MemberIdResult;
import com.chalnakchalnak.account.auth.application.result.SignInResult;

public interface AuthUseCase {

  void signUp(SignUpCommand signUpRequestDto);

  Boolean existsMemberId(ExistsMemberIdQuery existsMemberIdRequestDto);

  Boolean existsNickname(ExistsNicknameQuery existsNicknameRequestDto);

  Boolean existsPhoneNumber(ExistsPhoneNumberQuery existsPhoneNumberRequestDto);

  SignInResult signIn(SignInCommand authSignInRequestDto);

  SignInResult reissueAllToken(ReissueAllTokenCommand reissueAllTokenRequestDto);

  void signOut(SignOutCommand signOutDto);

  MemberIdResult getMemberId(GetMemberIdQuery getMemberIdRequestDto);

  void resetPassword(ResetPasswordCommand resetPasswordRequestDto);
}
