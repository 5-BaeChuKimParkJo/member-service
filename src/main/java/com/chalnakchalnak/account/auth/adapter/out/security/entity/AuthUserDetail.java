package com.chalnakchalnak.account.auth.adapter.out.security.entity;

import com.chalnakchalnak.account.auth.adapter.out.persistence.postgresql.entity.AuthEntity;
import java.util.Collection;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Getter
@NoArgsConstructor
public class AuthUserDetail implements UserDetails {

  private String authUuid;
  private String userId;
  private String password;

  public AuthUserDetail(AuthEntity authEntity) {
    this.authUuid = authEntity.getMemberUuid();
    this.userId = authEntity.getMemberId();
    this.password = authEntity.getPassword();
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return null;
  }

  @Override
  public String getPassword() {
    return this.password;
  }

  @Override
  public String getUsername() {
    return this.userId;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return false;
  }
}
