package com.chalnakchalnak.member_service;

import com.chalnakchalnak.member_service.application.MemberService;
import com.chalnakchalnak.member_service.auth.adapter.in.web.mapper.AuthVoMapper;
import com.chalnakchalnak.member_service.auth.adapter.in.web.presentation.AuthController;
import com.chalnakchalnak.member_service.auth.application.port.in.AuthUseCase;
import com.chalnakchalnak.member_service.dto.out.MemberResponseDto;
import com.chalnakchalnak.member_service.entity.State;
import com.chalnakchalnak.member_service.presentation.MemberController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class LegacyRouteCompatibilityTest {

    private MockMvc mockMvc;
    private AuthUseCase authUseCase;
    private MemberService memberService;

    @BeforeEach
    void setUp() {
        authUseCase = mock(AuthUseCase.class);
        memberService = mock(MemberService.class);
        mockMvc = MockMvcBuilders.standaloneSetup(
                        new AuthController(authUseCase, new AuthVoMapper()),
                        new MemberController(memberService)
                )
                .build();
    }

    @Test
    void preservesLegacyAuthSignupRoute() throws Exception {
        mockMvc.perform(post("/auth-service/api/v1/auth/sign-up")
                        .contentType("application/json")
                        .content("""
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

        mockMvc.perform(get("/member-service/api/v1/member")
                        .header("X-Member-Uuid", "member-uuid"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.memberUuid").value("member-uuid"))
                .andExpect(jsonPath("$.nickname").value("member"))
                .andExpect(jsonPath("$.gradeUuid").value("grade-uuid"))
                .andExpect(jsonPath("$.point").value(100.0));
    }

    @Test
    void exposesCanonicalAccountRoutes() throws Exception {
        when(memberService.getMember("member-uuid")).thenReturn(memberResponse());

        mockMvc.perform(get("/account-service/api/v1/member/member-uuid"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.memberUuid").value("member-uuid"));
    }

    private MemberResponseDto memberResponse() {
        return MemberResponseDto.builder()
                .memberUuid("member-uuid")
                .nickname("member")
                .gradeUuid("grade-uuid")
                .state(State.ACTIVE)
                .point(100.0)
                .build();
    }
}
