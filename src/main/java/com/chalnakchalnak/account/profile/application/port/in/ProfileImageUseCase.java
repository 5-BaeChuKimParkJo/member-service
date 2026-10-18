package com.chalnakchalnak.account.profile.application.port.in;

import com.chalnakchalnak.account.profile.application.command.PrepareProfileImageCommand;
import com.chalnakchalnak.account.profile.application.command.SaveProfileImageCommand;
import com.chalnakchalnak.account.profile.application.result.ProfileImageUploadResult;

public interface ProfileImageUseCase {

  ProfileImageUploadResult generatePresignedPost(PrepareProfileImageCommand presignedUrlRequestDto);

  void saveImageUrl(SaveProfileImageCommand saveImageUrlRequestDto);
}
