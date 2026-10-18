package com.chalnakchalnak.account.profile.adapter.in.web;

import com.chalnakchalnak.account.profile.adapter.in.web.request.PrepareProfileImageRequest;
import com.chalnakchalnak.account.profile.adapter.in.web.request.SaveProfileImageRequest;
import com.chalnakchalnak.account.profile.adapter.in.web.response.ProfileImageUploadResponse;
import com.chalnakchalnak.account.profile.application.command.PrepareProfileImageCommand;
import com.chalnakchalnak.account.profile.application.command.SaveProfileImageCommand;
import com.chalnakchalnak.account.profile.application.port.in.ProfileImageUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Tag(name = "ProfileImage", description = "프로필 이미지 관련 API")
@RequiredArgsConstructor
@RestController
@RequestMapping({
  "/api/v1/member",
  "/member-service/api/v1/member",
  "/account-service/api/v1/member"
})
public class ProfileImageUploadController {

  private final ProfileImageUseCase presignedUrlService;

  @Operation(summary = "AWS S3 Presigned url 요청")
  @PostMapping("/presigned-url")
  public ProfileImageUploadResponse getPresignedUrl(
      @RequestHeader("X-Member-Uuid") String memberUuid,
      @RequestBody @Valid PrepareProfileImageRequest presignedUrlRequestVo) {

    var result =
        presignedUrlService.generatePresignedPost(
            PrepareProfileImageCommand.toPresignedUrlRequestDto(
                presignedUrlRequestVo.getContentType(), memberUuid));
    return ProfileImageUploadResponse.builder()
        .url(result.getUrl())
        .fields(result.getFields())
        .build();
  }

  @Operation(summary = "프로필 이미지 key DB저장")
  @PutMapping("/save-url")
  public void saveImageUrl(
      @RequestHeader("X-Member-Uuid") String memberUuid,
      @RequestBody @Valid SaveProfileImageRequest saveImageUrlRequestVo) {
    presignedUrlService.saveImageUrl(
        SaveProfileImageCommand.builder()
            .memberUuid(memberUuid)
            .profileImageKey(saveImageUrlRequestVo.getProfileImageKey())
            .build());
  }
}
