package com.chalnakchalnak.member_service.auth.adapter.out.http.feign.member;

import com.chalnakchalnak.member_service.auth.application.port.dto.feign.member.CreateMemberRequestDto;
import com.chalnakchalnak.member_service.auth.application.port.out.feign.member.MemberServicePort;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        name = "member-service",
        path = "api/v1/member",
        url = "${feign.client.member-service.url}",
        fallbackFactory = MemberServiceFallbackFactory.class
)
public interface MemberServiceFeignClient extends MemberServicePort {

    @PostMapping("/sign-up")
    void createMember(@RequestBody CreateMemberRequestDto createMemberRequestDto);

    @GetMapping("/exists/nickname/{nickname}")
    Boolean existsByNickname(@PathVariable String nickname);
}
