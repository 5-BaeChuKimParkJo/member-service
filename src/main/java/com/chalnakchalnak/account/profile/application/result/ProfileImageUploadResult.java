package com.chalnakchalnak.account.profile.application.result;

import java.util.Map;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ProfileImageUploadResult {

  private String url;
  private Map<String, String> fields;

  @Builder
  public ProfileImageUploadResult(String url, Map<String, String> fields) {
    this.url = url;
    this.fields = fields;
  }
}
