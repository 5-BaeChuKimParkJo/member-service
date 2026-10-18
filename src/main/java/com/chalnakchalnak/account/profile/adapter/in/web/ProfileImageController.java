package com.chalnakchalnak.account.profile.adapter.in.web;

import com.chalnakchalnak.account.profile.application.service.ProfileImageService;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping({
  "/api/v1/member",
  "/member-service/api/v1/member",
  "/account-service/api/v1/member"
})
public class ProfileImageController {
  private static final Pattern PROFILE_KEY =
      Pattern.compile(
          "^member/[0-9a-fA-F-]{36}/images/[0-9a-fA-F-]{36}\\.(png|jpeg|jpg|webp|bmp)$");
  private final ProfileImageService service;

  public ProfileImageController(ProfileImageService service) {
    this.service = service;
  }

  @GetMapping("/images")
  public ResponseEntity<byte[]> read(@RequestParam String key) {
    if (!PROFILE_KEY.matcher(key).matches()) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid profile image key");
    }
    var image = service.read(key);
    return ResponseEntity.ok()
        .contentType(MediaType.parseMediaType(image.contentType()))
        .cacheControl(CacheControl.maxAge(365, TimeUnit.DAYS).cachePublic().immutable())
        .body(image.body());
  }
}
