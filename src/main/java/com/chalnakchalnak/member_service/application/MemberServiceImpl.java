package com.chalnakchalnak.member_service.application;

import com.chalnakchalnak.member_service.common.entity.BaseResponseStatus;
import com.chalnakchalnak.member_service.common.exception.BaseException;
import com.chalnakchalnak.member_service.dto.in.MemberUpdateRequestDto;
import com.chalnakchalnak.member_service.dto.in.MemberUuidListDto;
import com.chalnakchalnak.member_service.dto.in.SignUpRequestDto;
import com.chalnakchalnak.member_service.dto.out.MemberResponseDto;
import com.chalnakchalnak.member_service.entity.Member;
import com.chalnakchalnak.member_service.infrastructure.MemberRepository;
import com.chalnakchalnak.member_service.infrastructure.custom.MemberRepositoryCustom;
import com.chalnakchalnak.member_service.util.CacheUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final MemberRepositoryCustom memberRepositoryCustom;
    private final RedisTemplate<String, Object> redisTemplate;
    private final CacheUtil cacheUtil;

    @Cacheable(value = "member", key = "#memberUuid")
    @Override
    public MemberResponseDto getMember(String memberUuid) {
        return MemberResponseDto.from(memberRepository.findByMemberUuid(memberUuid)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.NO_EXISTS_MEMBER)));
    }

    @Cacheable(value = "memberList", key = "#memberUuidListDto.memberUuidList")
    @Override
    public List<MemberResponseDto> getMemberList(MemberUuidListDto memberUuidListDto) {
        return memberUuidListDto.getMemberUuidList()
                .stream()
                .map(memberUuid -> MemberResponseDto.from(memberRepository.findByMemberUuid(memberUuid)
                        .orElseThrow(() -> new BaseException(BaseResponseStatus.NO_EXISTS_MEMBER))))
                .toList();
    }

    @Override
    public List<MemberResponseDto> getAllMemberList() {
        return memberRepository.findAll()
                .stream()
                .map(MemberResponseDto::from)
                .toList();
    }

    @Override
    public void updateDynamic(MemberUpdateRequestDto memberUpdateRequestDto) {

        Member member = memberRepository.findByMemberUuid(memberUpdateRequestDto.getMemberUuid())
                .orElseThrow(() -> new BaseException(BaseResponseStatus.NO_EXISTS_MEMBER));

        if (!"".equals(memberUpdateRequestDto.getNickname())) {

            if (member.getNickname().equals(memberUpdateRequestDto.getNickname())) {
                throw new BaseException(BaseResponseStatus.ALREADY_USED_NICKNAME);
            }

            if (memberRepository.existsByNickname(memberUpdateRequestDto.getNickname())) {
                throw new BaseException(BaseResponseStatus.DUPLICATE_NICKNAME);
            }
        }

        memberRepositoryCustom.updateDynamic(memberUpdateRequestDto);

        // 관련 캐시 삭제
        cacheUtil.evictMemberCache("member" , memberUpdateRequestDto.getMemberUuid());
        cacheUtil.evictMemberCacheList("memberList" , memberUpdateRequestDto.getMemberUuid());
    }

    @Override
    public void deleteMember(String memberUuid) {
        Member member = memberRepository.findByMemberUuid(memberUuid)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.NO_EXISTS_MEMBER));
        memberRepository.delete(member);

        // 관련 캐시 삭제
        cacheUtil.evictMemberCache("member" , memberUuid);
        cacheUtil.evictMemberCacheList("memberList" , memberUuid);
    }

    @Override
    @Transactional
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
