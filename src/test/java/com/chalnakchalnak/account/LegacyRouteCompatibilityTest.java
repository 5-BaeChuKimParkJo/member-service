package com.chalnakchalnak.account;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.chalnakchalnak.account.auth.adapter.in.web.AuthController;
import com.chalnakchalnak.account.auth.adapter.in.web.mapper.AuthWebMapper;
import com.chalnakchalnak.account.auth.application.port.in.AuthUseCase;
import com.chalnakchalnak.account.member.adapter.in.web.MemberController;
import com.chalnakchalnak.account.member.application.command.UpdateMemberCommand;
import com.chalnakchalnak.account.member.application.port.in.MemberUseCase;
import com.chalnakchalnak.account.member.application.result.MemberResult;
import com.chalnakchalnak.account.member.domain.State;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

class LegacyRouteCompatibilityTest {

  private MockMvc mockMvc;
  private AuthUseCase authUseCase;
  private MemberUseCase memberService;

  @BeforeEach
  void setUp() {
    authUseCase = mock(AuthUseCase.class);
    memberService = mock(MemberUseCase.class);
    mockMvc =
        MockMvcBuilders.standaloneSetup(
                new AuthController(authUseCase, new AuthWebMapper()),
                new MemberController(memberService))
            .build();
  }

  @Test
  void preservesLegacyAuthSignupRoute() throws Exception {
    mockMvc
        .perform(
            post("/auth-service/api/v1/auth/sign-up")
                .contentType("application/json")
                .content(
                    """
                                {
                                  "memberId": "member01",
                                  "password": "Password1!",
                                  "nickname": "member",
                                  "phoneNumber": "01012345678"
                                }
                                """))
        .andExpect(status().isOk());

    verify(authUseCase).signUp(any());
  }

  @Test
  void preservesLegacyMemberReadResponse() throws Exception {
    when(memberService.getMember("member-uuid")).thenReturn(memberResponse());

    mockMvc
        .perform(get("/member-service/api/v1/member").header("X-Member-Uuid", "member-uuid"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.memberUuid").value("member-uuid"))
        .andExpect(jsonPath("$.nickname").value("member"))
        .andExpect(jsonPath("$.gradeUuid").value("grade-uuid"))
        .andExpect(jsonPath("$.point").value(100.0));
  }

  @Test
  void exposesCanonicalAccountRoutes() throws Exception {
    when(memberService.getMember("member-uuid")).thenReturn(memberResponse());

    mockMvc
        .perform(get("/account-service/api/v1/member/member-uuid"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.memberUuid").value("member-uuid"));
  }

  @Test
  void profileUpdateCannotOverwriteAccountOwnedState() throws Exception {
    mockMvc
        .perform(
            put("/member-service/api/v1/member/update")
                .header("X-Member-Uuid", "member-uuid")
                .contentType("application/json")
                .content(
                    """
                                {
                                  "nickname": "updated",
                                  "profileImageKey": "profiles/updated.png",
                                  "gradeUuid": "admin-grade",
                                  "honor": "REAL_MAN",
                                  "state": "BLOCKED",
                                  "point": 999999
                                }
                                """))
        .andExpect(status().isOk());

    ArgumentCaptor<UpdateMemberCommand> captor = ArgumentCaptor.forClass(UpdateMemberCommand.class);
    verify(memberService).updateDynamic(captor.capture());
    UpdateMemberCommand update = captor.getValue();
    assertThat(update.getNickname()).isEqualTo("updated");
    assertThat(update.getProfileImageKey()).isEqualTo("profiles/updated.png");
    assertThat(update.getGradeUuid()).isNull();
    assertThat(update.getHonor()).isNull();
    assertThat(update.getState()).isNull();
    assertThat(update.getPoint()).isNull();
  }

  private MemberResult memberResponse() {
    return MemberResult.builder()
        .memberUuid("member-uuid")
        .nickname("member")
        .gradeUuid("grade-uuid")
        .state(State.ACTIVE)
        .point(100.0)
        .build();
  }
}
