package com.chalnakchalnak.account.dto.out;

import com.chalnakchalnak.account.entity.Honor;
import com.chalnakchalnak.account.entity.Member;
import com.chalnakchalnak.account.entity.State;
import com.chalnakchalnak.account.vo.out.MemberResponseVo;
import com.chalnakchalnak.account.util.ProfileImageUrls;
import lombok.Builder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.Serializable;

public class MemberResponseDto implements Serializable {

    private String memberUuid;
    private String nickname;
    private String gradeUuid;
    private Honor honor;
    private State state;
    private String profileImageUrl;
    private Double point;

    @Builder
    public MemberResponseDto(String memberUuid,
                             String nickname,
                             String gradeUuid,
                             Honor honor,
                             State state,
                             String profileImageUrl,
                             Double point) {
        this.memberUuid = memberUuid;
        this.nickname = nickname;
        this.gradeUuid = gradeUuid;
        this.honor = honor;
        this.state = state;
        this.profileImageUrl = profileImageUrl;
        this.point = point;
    }

    public static MemberResponseDto from(Member member, String bucket, String region) {
        return from(member, bucket, region, "");
    }

    public static MemberResponseDto from(Member member, String bucket, String region, String publicBaseUrl) {

        String imageUrl = ProfileImageUrls.resolve(member.getProfileImageKey(), bucket, region, publicBaseUrl);

        return MemberResponseDto.builder()
                .memberUuid(member.getMemberUuid())
                .nickname(member.getNickname())
                .gradeUuid(member.getGradeUuid())
                .honor(member.getHonor())
                .state(member.getState())
                .profileImageUrl(imageUrl)
                .point(member.getPoint())
                .build();
    }

    public MemberResponseVo toVo() {
        return MemberResponseVo.builder()
                .memberUuid(memberUuid)
                .nickname(nickname)
                .gradeUuid(gradeUuid)
                .honor(honor)
                .state(state)
                .profileImageUrl(profileImageUrl)
                .point(point)
                .build();
    }
}
