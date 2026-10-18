package com.chalnakchalnak.member_service.util;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class ProfileImageUrlsTest {
    @Test
    void usesThePublicMemberMediaRouteForConfiguredStorage() {
        assertThat(ProfileImageUrls.resolve("member/user/images/photo.png", "images", "region",
                "/member-service/api/v1/member/images"))
                .isEqualTo("/member-service/api/v1/member/images?key=member/user/images/photo.png");
    }

    @Test
    void doesNotRenderMissingDefaultProfileObjects() {
        assertThat(ProfileImageUrls.resolve(null, "images", "region", "")).isNull();
        assertThat(ProfileImageUrls.resolve("member/temp_member_uuid/images/default.png", "images", "region", "")).isNull();
    }
}
