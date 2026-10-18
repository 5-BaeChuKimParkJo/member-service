package com.chalnakchalnak.account.auth.adapter.out.persistence.postgresql.entity;

import com.chalnakchalnak.account.auth.adapter.out.persistence.postgresql.common.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(
    name = "auth",
    indexes = {
      @Index(name = "idx_member_id", columnList = "member_id"),
      @Index(name = "idx_phone_number", columnList = "phone_number")
    })
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AuthEntity extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @JsonIgnore
  private Long id;

  @Column(name = "member_uuid", unique = true, nullable = false, length = 50)
  private String memberUuid;

  @Column(name = "member_id", unique = true, nullable = false, length = 100)
  private String memberId;

  @Column(name = "password", nullable = false, length = 100)
  private String password;

  @Column(name = "phone_number", unique = true, nullable = false, length = 20)
  private String phoneNumber;

  @Builder
  public AuthEntity(String memberUuid, String memberId, String password, String phoneNumber) {
    this.memberUuid = memberUuid;
    this.memberId = memberId;
    this.password = password;
    this.phoneNumber = phoneNumber;
  }

  public void resetPassword(String password) {
    this.password = password;
  }
}
