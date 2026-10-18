package com.chalnakchalnak.account.application;

import com.chalnakchalnak.account.common.entity.BaseResponseStatus;
import com.chalnakchalnak.account.common.exception.BaseException;
import com.chalnakchalnak.account.dto.in.MemberUpdateRequestDto;
import com.chalnakchalnak.account.dto.in.MemberUuidListDto;
import com.chalnakchalnak.account.dto.in.SignUpRequestDto;
import com.chalnakchalnak.account.dto.out.ChatroomMemberResponseDto;
import com.chalnakchalnak.account.dto.out.MemberResponseDto;
import com.chalnakchalnak.account.entity.Member;
import com.chalnakchalnak.account.infrastructure.MemberRepository;
import com.chalnakchalnak.account.infrastructure.custom.MemberRepositoryCustom;
import com.chalnakchalnak.account.grade.GradeRepository;
import com.chalnakchalnak.account.util.CacheUtil;
import com.chalnakchalnak.account.common.config.MemberCacheNames;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.region.static}")
    private String region;

    @Value("${cloud.aws.s3.public-base-url:}")
    private String profileImageBaseUrl;

    private final MemberRepository memberRepository;
    private final MemberRepositoryCustom memberRepositoryCustom;
    private final GradeRepository gradeRepository;
    private final CacheUtil cacheUtil;

    @Cacheable(value = MemberCacheNames.CHAT_MEMBER, key = "#memberUuid")
    @Override
    public ChatroomMemberResponseDto getChatMember(String memberUuid) {
        Member member = memberRepository.findByMemberUuid(memberUuid)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.NO_EXISTS_MEMBER));
        return ChatroomMemberResponseDto.from(member, bucket, region, profileImageBaseUrl);
    }

    @Cacheable(value = MemberCacheNames.MEMBER, key = "#memberUuid")
    @Override
    public MemberResponseDto getMember(String memberUuid) {
        Member member = memberRepository.findByMemberUuid(memberUuid)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.NO_EXISTS_MEMBER));
        return MemberResponseDto.from(member, bucket, region, profileImageBaseUrl);
    }

    @Cacheable(value = MemberCacheNames.MEMBER_LIST, key = "#memberUuidListDto.memberUuidList")
    @Override
    public List<MemberResponseDto> getMemberList(MemberUuidListDto memberUuidListDto) {
        List<Member> memberList = memberUuidListDto.getMemberUuidList()
                .stream()
                .map(memberUuid -> memberRepository.findByMemberUuid(memberUuid)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.NO_EXISTS_MEMBER)))
                .toList();

        List<MemberResponseDto> memberResponseDtoList = new ArrayList<>();
        for (Member member : memberList) {
            memberResponseDtoList.add(MemberResponseDto.from(member, bucket, region, profileImageBaseUrl));
        }

        return memberResponseDtoList;
    }

    @Override
    public List<MemberResponseDto> getAllMemberList() {
         return memberRepository.findAll()
                .stream()
                .map(member -> MemberResponseDto.from(member, bucket, region, profileImageBaseUrl))
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
        cacheUtil.evictMemberCache(MemberCacheNames.MEMBER, memberUpdateRequestDto.getMemberUuid());
        cacheUtil.evictMemberCacheList(MemberCacheNames.MEMBER_LIST, memberUpdateRequestDto.getMemberUuid());
        cacheUtil.evictMemberCache(MemberCacheNames.CHAT_MEMBER, memberUpdateRequestDto.getMemberUuid());
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

        String defaultGradeUuid = gradeRepository.findByOrderNumber(5)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.GRADE_API_ERROR))
                .getGradeUuid();
        memberRepository.save(signUpRequestDto.toEntity(defaultGradeUuid));
    }

    @Override
    public Boolean existNickname(String nickname) {
        return memberRepository.existsByNickname(nickname);
    }
}
