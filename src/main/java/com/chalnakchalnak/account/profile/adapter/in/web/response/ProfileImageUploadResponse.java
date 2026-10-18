package com.chalnakchalnak.account.profile.adapter.in.web.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Map;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ProfileImageUploadResponse {

  @Schema(description = "S3 url", nullable = false)
  private String url;

  @Schema(description = "S3 요청 정보", nullable = false)
  private Map<String, String> fields;

  @Builder
  public ProfileImageUploadResponse(String url, Map<String, String> fields) {
    this.url = url;
    this.fields = fields;
  }
}
