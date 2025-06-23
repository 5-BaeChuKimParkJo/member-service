package com.chalnakchalnak.member_service.vo.out;

import com.chalnakchalnak.member_service.dto.out.ChatroomMemberResponseDto;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ChatroomMemberResponseVo {

    private String memberUuid;
    private String nickname;
    private String profileImageUrl;

    @Builder
    public ChatroomMemberResponseVo(String memberUuid,
                                    String nickname,
                                    String profileImageUrl) {
        this.memberUuid = memberUuid;
        this.nickname = nickname;
        this.profileImageUrl = profileImageUrl;
    }
}
