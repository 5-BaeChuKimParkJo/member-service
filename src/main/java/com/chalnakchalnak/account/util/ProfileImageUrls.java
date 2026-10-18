package com.chalnakchalnak.account.util;

import org.springframework.web.util.UriComponentsBuilder;

public final class ProfileImageUrls {
  private ProfileImageUrls() {}

  public static String resolve(String key, String bucket, String region, String publicBaseUrl) {
    if (key == null || key.isBlank() || key.startsWith("member/temp_member_uuid/")) return null;
    if (publicBaseUrl != null && !publicBaseUrl.isBlank()) {
      return UriComponentsBuilder.fromUriString(publicBaseUrl)
          .queryParam("key", key)
          .build()
          .encode()
          .toUriString();
    }
    return "https://" + bucket + ".s3." + region + ".amazonaws.com/" + key;
  }
}
