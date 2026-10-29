package com.chalnakchalnak.account.common;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.chalnakchalnak.account.auth.adapter.in.web.AuthController;
import com.chalnakchalnak.account.auth.adapter.in.web.IdentityVerificationController;
import com.chalnakchalnak.account.auth.adapter.in.web.mapper.AuthWebMapper;
import com.chalnakchalnak.account.auth.adapter.in.web.mapper.IdentityVerificationWebMapper;
import com.chalnakchalnak.account.auth.application.port.in.AuthUseCase;
import com.chalnakchalnak.account.auth.application.port.in.IdentityVerificationUseCase;
import com.chalnakchalnak.account.auth.common.exception.AuthBaseExceptionHandler;
import com.chalnakchalnak.account.auth.common.response.AuthErrorCode;
import com.chalnakchalnak.account.common.error.AccountErrorCode;
import com.chalnakchalnak.account.common.exception.BaseException;
import com.chalnakchalnak.account.common.exception.BaseExceptionHandler;
import com.chalnakchalnak.account.member.adapter.in.web.MemberController;
import com.chalnakchalnak.account.member.application.port.in.MemberUseCase;
import com.chalnakchalnak.account.profile.adapter.in.web.ProfileImageUploadController;
import com.chalnakchalnak.account.profile.application.port.in.ProfileImageUseCase;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

class MemberErrorResponseTest {
  private static final String[] MEMBER_ROUTES = {
    "/api/v1/member", "/member-service/api/v1/member", "/account-service/api/v1/member"
  };
  private static final String[] AUTH_ROUTES = {
    "/api/v1/auth", "/auth-service/api/v1/auth", "/account-service/api/v1/auth"
  };
  private static final String SIGN_IN =
      """
      {"memberId":"fixture-user","password":"Fixture1!"}
      """;

  private final MemberUseCase members = mock(MemberUseCase.class);
  private final AuthUseCase auth = mock(AuthUseCase.class);
  private final IdentityVerificationUseCase verification = mock(IdentityVerificationUseCase.class);

  private MockMvc mvc(boolean authFirst) {
    Object commonAdvice = new BaseExceptionHandler();
    Object authAdvice = new AuthBaseExceptionHandler();
    return MockMvcBuilders.standaloneSetup(
            new MemberController(members),
            new ProfileImageUploadController(mock(ProfileImageUseCase.class)),
            new AuthController(auth, new AuthWebMapper()),
            new IdentityVerificationController(verification, new IdentityVerificationWebMapper()))
        .setControllerAdvice(
            authFirst
                ? new Object[] {authAdvice, commonAdvice}
                : new Object[] {commonAdvice, authAdvice})
        .build();
  }

  @ParameterizedTest
  @ValueSource(booleans = {true, false})
  void absentMemberReturns404OnEveryAliasRegardlessOfAdviceOrder(boolean authFirst)
      throws Exception {
    when(members.getMember("absent-member"))
        .thenThrow(new BaseException(AccountErrorCode.NO_EXISTS_MEMBER));
    var mvc = mvc(authFirst);
    for (String route : MEMBER_ROUTES) {
      mvc.perform(get(route + "/absent-member"))
          .andExpect(status().isNotFound())
          .andExpect(
              content()
                  .json(
                      """
              {"code":2002,"message":"존재하지 않는 회원입니다."}
              """))
          .andExpect(jsonPath("$.httpStatus").doesNotExist());
    }
  }

  @ParameterizedTest
  @ValueSource(booleans = {true, false})
  void memberNicknameConflictsRetainTheir409Codes(boolean authFirst) throws Exception {
    var mvc = mvc(authFirst);
    for (AccountErrorCode code :
        new AccountErrorCode[] {
          AccountErrorCode.DUPLICATE_NICKNAME, AccountErrorCode.ALREADY_USED_NICKNAME
        }) {
      doThrow(new BaseException(code)).when(members).updateDynamic(any());
      mvc.perform(
              put("/api/v1/member/update")
                  .header("X-Member-Uuid", "fixture-member")
                  .contentType("application/json")
                  .content("{\"nickname\":\"fixture-name\"}"))
          .andExpect(status().isConflict())
          .andExpect(jsonPath("$.code").value(code.getCode()))
          .andExpect(jsonPath("$.message").value(code.getMessage()));
    }
  }

  @ParameterizedTest
  @ValueSource(booleans = {true, false})
  void databaseFailureRemains500WithoutLeakingItsMessage(boolean authFirst) throws Exception {
    when(members.getMember("fixture-member"))
        .thenThrow(
            new DataAccessResourceFailureException("private SQL connection/password fixture"));
    mvc(authFirst)
        .perform(get("/api/v1/member/fixture-member"))
        .andExpect(status().isInternalServerError())
        .andExpect(
            content()
                .json(
                    """
            {"code":500,"message":"서버 내부 오류가 발생했습니다. 관리자에게 문의해주세요."}
            """))
        .andExpect(jsonPath("$.memberUuid").doesNotExist());
  }

  @ParameterizedTest
  @ValueSource(booleans = {true, false})
  void authDomainFailureRetainsIts401OnEveryAlias(boolean authFirst) throws Exception {
    when(auth.signIn(any()))
        .thenThrow(
            new com.chalnakchalnak.account.auth.common.exception.BaseException(
                AuthErrorCode.FAILED_TO_LOGIN));
    var mvc = mvc(authFirst);
    for (String route : AUTH_ROUTES) {
      mvc.perform(post(route + "/sign-in").contentType("application/json").content(SIGN_IN))
          .andExpect(status().isUnauthorized())
          .andExpect(jsonPath("$.code").value(4204))
          .andExpect(jsonPath("$.message").value(AuthErrorCode.FAILED_TO_LOGIN.getMessage()));
    }
  }

  @ParameterizedTest
  @ValueSource(booleans = {true, false})
  void authInfrastructureFailureRetainsItsGeneric500(boolean authFirst) throws Exception {
    when(auth.signIn(any()))
        .thenThrow(new DataAccessResourceFailureException("private auth database fixture"));
    mvc(authFirst)
        .perform(post("/api/v1/auth/sign-in").contentType("application/json").content(SIGN_IN))
        .andExpect(status().isInternalServerError())
        .andExpect(jsonPath("$.code").value(4903))
        .andExpect(jsonPath("$.message").value(AuthErrorCode.INTERNAL_SERVER_ERROR.getMessage()));
  }

  @ParameterizedTest
  @ValueSource(booleans = {true, false})
  void securityBadCredentialsRetainTheir401(boolean authFirst) throws Exception {
    when(auth.signIn(any())).thenThrow(new BadCredentialsException("private credential fixture"));
    mvc(authFirst)
        .perform(post("/api/v1/auth/sign-in").contentType("application/json").content(SIGN_IN))
        .andExpect(status().isUnauthorized())
        .andExpect(jsonPath("$.code").value(4204))
        .andExpect(jsonPath("$.message").value(AuthErrorCode.FAILED_TO_LOGIN.getMessage()));
  }

  @ParameterizedTest
  @ValueSource(booleans = {true, false})
  void authValidationRetainsIts400AndPublicMessage(boolean authFirst) throws Exception {
    mvc(authFirst)
        .perform(
            post("/api/v1/auth/sign-in")
                .contentType("application/json")
                .content("{\"memberId\":\"\",\"password\":\"Fixture1!\"}"))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.code").value(4905))
        .andExpect(jsonPath("$.message").value("아이디를 입력해주세요."));
  }

  @ParameterizedTest
  @ValueSource(booleans = {true, false})
  void malformedJsonUsesTheOwningApiErrorFamily(boolean authFirst) throws Exception {
    var mvc = mvc(authFirst);
    mvc.perform(post("/api/v1/member/list").contentType("application/json").content("{"))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.code").value(902));
    mvc.perform(post("/api/v1/auth/sign-in").contentType("application/json").content("{"))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.code").value(4905));
  }

  @ParameterizedTest
  @ValueSource(booleans = {true, false})
  void unsupportedProfileFormatRetainsAnActionableMessage(boolean authFirst) throws Exception {
    mvc(authFirst)
        .perform(
            post("/api/v1/member/presigned-url")
                .header("X-Member-Uuid", "fixture-member")
                .contentType("application/json")
                .content("{\"contentType\":\"application/pdf\"}"))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.code").value(902))
        .andExpect(jsonPath("$.message").value("지원하지 않는 이미지 형식입니다."));
  }

  @ParameterizedTest
  @ValueSource(booleans = {true, false})
  void unsupportedAuthAndVerificationMethodsRetainAuthCodes(boolean authFirst) throws Exception {
    var mvc = mvc(authFirst);
    for (String route : AUTH_ROUTES) {
      mvc.perform(get(route + "/sign-in"))
          .andExpect(status().isMethodNotAllowed())
          .andExpect(jsonPath("$.code").value(4902));
      String verificationRoute =
          route.substring(0, route.length() - "/auth".length()) + "/identity-verification";
      mvc.perform(get(verificationRoute + "/sms/verify"))
          .andExpect(status().isMethodNotAllowed())
          .andExpect(jsonPath("$.code").value(4902));
      mvc.perform(get("/fixture-context" + route + "/sign-in").contextPath("/fixture-context"))
          .andExpect(status().isMethodNotAllowed())
          .andExpect(jsonPath("$.code").value(4902));
    }
  }

  @ParameterizedTest
  @ValueSource(booleans = {true, false})
  void unsupportedMemberMethodsUseTheCommonErrorCode(boolean authFirst) throws Exception {
    var mvc = mvc(authFirst);
    for (String route : MEMBER_ROUTES) {
      mvc.perform(delete(route + "/fixture-member"))
          .andExpect(status().isMethodNotAllowed())
          .andExpect(jsonPath("$.code").value(405));
    }
  }

  @ParameterizedTest
  @ValueSource(booleans = {true, false})
  void verificationDomainFailureRetainsIts400(boolean authFirst) throws Exception {
    when(verification.verifyCode(any()))
        .thenThrow(
            new com.chalnakchalnak.account.auth.common.exception.BaseException(
                AuthErrorCode.EXPIRED_VERIFICATION_CODE));
    mvc(authFirst)
        .perform(
            post("/api/v1/identity-verification/sms/verify")
                .contentType("application/json")
                .content(
                    """
                {"phoneNumber":"01000000000","verificationCode":"123456","purpose":"SIGN_UP"}
                """))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.code").value(4200))
        .andExpect(
            jsonPath("$.message").value(AuthErrorCode.EXPIRED_VERIFICATION_CODE.getMessage()));
  }
}
