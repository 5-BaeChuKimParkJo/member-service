package com.chalnakchalnak.account.auth.application.result;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
public class AuthResult {

  private String memberUuid;
  private String memberId;
  private String password;
  private String phoneNumber;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

  @Builder
  public AuthResult(
      String memberUuid,
      String memberId,
      String password,
      String phoneNumber,
      LocalDateTime createdAt,
      LocalDateTime updatedAt) {
    this.memberUuid = memberUuid;
    this.memberId = memberId;
    this.password = password;
    this.phoneNumber = phoneNumber;
    this.createdAt = createdAt;
    this.updatedAt = updatedAt;
  }
}
