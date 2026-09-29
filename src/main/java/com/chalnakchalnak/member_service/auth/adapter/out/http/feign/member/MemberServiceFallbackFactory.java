package com.chalnakchalnak.member_service.auth.adapter.out.http.feign.member;

import com.chalnakchalnak.member_service.auth.application.port.dto.feign.member.CreateMemberRequestDto;
import com.chalnakchalnak.member_service.auth.common.exception.BaseException;
import com.chalnakchalnak.member_service.auth.common.response.BaseResponseStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MemberServiceFallbackFactory implements FallbackFactory<MemberServiceFeignClient> {

    @Override
    public MemberServiceFeignClient create(Throwable cause) {
        return new MemberServiceFeignClient() {
            @Override
            public void createMember(CreateMemberRequestDto dto) {
                // 예외 핸들링 로직
                throw new BaseException(BaseResponseStatus.MEMBER_SERVICE_CREATE_ERROR);
            }

            @Override
            public Boolean existsByNickname(String nickname) {
                throw new BaseException(BaseResponseStatus.MEMBER_SERVICE_EXISTS_NICKNAME_ERROR);
            }
        };
    }
}
