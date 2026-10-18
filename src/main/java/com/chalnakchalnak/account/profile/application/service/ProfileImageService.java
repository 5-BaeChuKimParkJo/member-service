package com.chalnakchalnak.account.profile.application.service;

import com.chalnakchalnak.account.member.application.port.out.MemberRepositoryPort;
import com.chalnakchalnak.account.profile.application.command.PrepareProfileImageCommand;
import com.chalnakchalnak.account.profile.application.command.SaveProfileImageCommand;
import com.chalnakchalnak.account.profile.application.port.in.ProfileImageUseCase;
import com.chalnakchalnak.account.profile.application.port.out.ProfileImageStoragePort;
import com.chalnakchalnak.account.profile.application.result.ProfileImageUploadResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProfileImageService implements ProfileImageUseCase {
  private final MemberRepositoryPort members;
  private final ProfileImageStoragePort storage;

  @Override
  public ProfileImageUploadResult generatePresignedPost(PrepareProfileImageCommand command) {
    return storage.generatePresignedPost(command);
  }

  @Override
  public void saveImageUrl(SaveProfileImageCommand command) {
    members.updateProfileImage(command.getMemberUuid(), command.getProfileImageKey());
  }

  public ProfileImageStoragePort.ProfileImageContent read(String key) {
    return storage.read(key);
  }
}
