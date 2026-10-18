package com.chalnakchalnak.account.profile.application.port.out;

import com.chalnakchalnak.account.profile.application.command.PrepareProfileImageCommand;
import com.chalnakchalnak.account.profile.application.result.ProfileImageUploadResult;

public interface ProfileImageStoragePort {
  ProfileImageUploadResult generatePresignedPost(PrepareProfileImageCommand command);

  ProfileImageContent read(String key);

  record ProfileImageContent(byte[] body, String contentType) {}
}
