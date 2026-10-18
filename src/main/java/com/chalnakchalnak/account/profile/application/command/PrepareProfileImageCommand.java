package com.chalnakchalnak.account.profile.application.command;

import java.util.UUID;
import lombok.Builder;
import lombok.Getter;

@Getter
public class PrepareProfileImageCommand {

  private String key;
  private String contentType;

  @Builder
  public PrepareProfileImageCommand(String key, String contentType) {
    this.key = key;
    this.contentType = contentType;
  }

  public static PrepareProfileImageCommand toPresignedUrlRequestDto(
      String contentType, String memberUuid) {
    String ext = contentType.substring(contentType.indexOf("/") + 1);

    return PrepareProfileImageCommand.builder()
        .key("member/" + memberUuid + "/" + "images/" + UUID.randomUUID() + "." + ext)
        .contentType(contentType)
        .build();
  }
}
