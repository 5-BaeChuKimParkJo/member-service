package com.chalnakchalnak.member_service.application;

import com.chalnakchalnak.member_service.common.entity.BaseResponseStatus;
import com.chalnakchalnak.member_service.common.exception.BaseException;
import com.chalnakchalnak.member_service.dto.in.MemberUpdateRequestDto;
import com.chalnakchalnak.member_service.dto.in.MemberUuidListDto;
import com.chalnakchalnak.member_service.dto.in.SignUpRequestDto;
import com.chalnakchalnak.member_service.dto.out.MemberResponseDto;
import com.chalnakchalnak.member_service.infrastructure.MemberRepository;
import com.chalnakchalnak.member_service.infrastructure.custom.MemberRepositoryCustom;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final MemberRepositoryCustom memberRepositoryCustom;
    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public MemberResponseDto getMember(String memberUuid) {
        return MemberResponseDto.from(memberRepository.findByMemberUuid(memberUuid)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.NO_EXISTS_MEMBER)));
    }

    @Override
    public List<MemberResponseDto> getMemberList(MemberUuidListDto memberUuidListDto) {
        return memberUuidListDto.getMemberUuidList()
                .stream()
                .map(memberUuid -> MemberResponseDto.from(memberRepository.findByMemberUuid(memberUuid)
                        .orElseThrow(() -> new BaseException(BaseResponseStatus.NO_EXISTS_MEMBER))))
                .toList();
    }

    @Override
    public void updateDynamic(MemberUpdateRequestDto memberUpdateRequestDto) {
        memberRepositoryCustom.updateDynamic(memberUpdateRequestDto);

//        // 관련 캐시 제거
//        Set<String> keys = redisTemplate.keys("memberUuidList::*" + memberUpdateRequestDto.getMemberUuid() + "*");
//        if (keys != null && !keys.isEmpty()) {
//            redisTemplate.delete(keys);
//        }
    }

    @Override
    public void signUp(SignUpRequestDto signUpRequestDto) {
        if (memberRepository.findByMemberUuid(signUpRequestDto.getMemberUuid()).isPresent()) {
            throw new BaseException(BaseResponseStatus.DUPLICATE_MEMBER);
        }

        Boolean existsNickname = memberRepository.existsByNickname(signUpRequestDto.getNickname());
        if (existsNickname) {
            throw new BaseException(BaseResponseStatus.DUPLICATE_NICKNAME);
        }

        String gradeUuid = "grade_tmp_uuid";        // 임시 등급 uuid
        memberRepository.save(signUpRequestDto.toEntity(gradeUuid));
    }

    @Override
    public Boolean existNickname(String nickname) {
        return memberRepository.existsByNickname(nickname);
    }
}
