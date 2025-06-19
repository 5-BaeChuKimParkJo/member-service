package com.chalnakchalnak.member_service.dto.out;

import com.chalnakchalnak.member_service.entity.Honor;
import com.chalnakchalnak.member_service.entity.Member;
import com.chalnakchalnak.member_service.entity.State;
import com.chalnakchalnak.member_service.vo.out.MemberResponseVo;
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
    private String profileImageKey;
    private Long point;

    @Builder
    public MemberResponseDto(String memberUuid,
                             String nickname,
                             String gradeUuid,
                             Honor honor,
                             State state,
                             String profileImageKey,
                             Long point) {
        this.memberUuid = memberUuid;
        this.nickname = nickname;
        this.gradeUuid = gradeUuid;
        this.honor = honor;
        this.state = state;
        this.profileImageKey = profileImageKey;
        this.point = point;
    }

    public static MemberResponseDto from(Member member) {
        return MemberResponseDto.builder()
                .memberUuid(member.getMemberUuid())
                .nickname(member.getNickname())
                .gradeUuid(member.getGradeUuid())
                .honor(member.getHonor())
                .state(member.getState())
                .profileImageKey(member.getProfileImageKey())
                .point(member.getPoint())
                .build();
    }

    public MemberResponseVo toVo(String bucket, String region) {
        String imageUrl = !"".equals(profileImageKey) && profileImageKey != null ?
                            "https://" + bucket + ".s3." + region + ".amazonaws.com/" + profileImageKey : null;

        String grade_tmp_uuid = "grade_tmp_uuid";

        return MemberResponseVo.builder()
                .memberUuid(memberUuid)
                .nickname(nickname)
                .gradeUuid(grade_tmp_uuid)
                .honor(honor)
                .state(state)
                .profileImageUrl(imageUrl)
                .point(point)
                .build();
    }
}
