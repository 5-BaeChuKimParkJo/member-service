package com.chalnakchalnak.account.presentation;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

@RestController
@RequestMapping({"/api/v1/member", "/member-service/api/v1/member", "/account-service/api/v1/member"})
public class ProfileImageController {
    private static final Pattern PROFILE_KEY = Pattern.compile(
            "^member/[0-9a-fA-F-]{36}/images/[0-9a-fA-F-]{36}\\.(png|jpeg|jpg|webp|bmp)$");
    private static final int MAX_BYTES = 10 * 1024 * 1024;
    private final AmazonS3 storage;
    private final String bucket;

    public ProfileImageController(AmazonS3 storage, @Value("${cloud.aws.s3.bucket}") String bucket) {
        this.storage = storage;
        this.bucket = bucket;
    }

    @GetMapping("/images")
    public ResponseEntity<byte[]> read(@RequestParam String key) {
        if (!PROFILE_KEY.matcher(key).matches()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid profile image key");
        }
        try (var object = storage.getObject(bucket, key)) {
            var contentType = object.getObjectMetadata().getContentType();
            if (contentType == null || !java.util.Set.of("image/png", "image/jpeg", "image/webp", "image/bmp").contains(contentType)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid profile image type");
            }
            byte[] body = object.getObjectContent().readNBytes(MAX_BYTES + 1);
            if (body.length > MAX_BYTES) {
                throw new ResponseStatusException(HttpStatus.PAYLOAD_TOO_LARGE);
            }
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .cacheControl(CacheControl.maxAge(365, TimeUnit.DAYS).cachePublic().immutable())
                    .body(body);
        } catch (AmazonS3Exception error) {
            throw new ResponseStatusException(error.getStatusCode() == 404 ? HttpStatus.NOT_FOUND : HttpStatus.BAD_GATEWAY);
        } catch (IOException error) {
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY);
        }
    }
}
