package com.chalnakchalnak.member_service.dto.out;

import com.chalnakchalnak.member_service.entity.Member;
import com.chalnakchalnak.member_service.vo.out.ChatroomMemberResponseVo;
import com.chalnakchalnak.member_service.util.ProfileImageUrls;
import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;

@Getter
public class ChatroomMemberResponseDto implements Serializable {

    private String memberUuid;
    private String nickname;
    private String profileImageUrl;

    @Builder
    public ChatroomMemberResponseDto(String memberUuid, String nickname, String profileImageUrl) {
        this.memberUuid = memberUuid;
        this.nickname = nickname;
        this.profileImageUrl = profileImageUrl;
    }

    public static ChatroomMemberResponseDto from(Member member, String bucket, String region) {
        return from(member, bucket, region, "");
    }

    public static ChatroomMemberResponseDto from(Member member, String bucket, String region, String publicBaseUrl) {
        String imageUrl = ProfileImageUrls.resolve(member.getProfileImageKey(), bucket, region, publicBaseUrl);
        return ChatroomMemberResponseDto.builder()
                .memberUuid(member.getMemberUuid())
                .nickname(member.getNickname())
                .profileImageUrl(imageUrl)
                .build();
    }

    public ChatroomMemberResponseVo toVo() {
        return ChatroomMemberResponseVo.builder()
                .memberUuid(memberUuid)
                .nickname(nickname)
                .profileImageUrl(profileImageUrl)
                .build();
    }
}
