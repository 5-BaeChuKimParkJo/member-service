package com.chalnakchalnak.account.profile.adapter.out.storage;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.chalnakchalnak.account.profile.application.command.PrepareProfileImageCommand;
import com.chalnakchalnak.account.profile.application.port.out.ProfileImageStoragePort;
import com.chalnakchalnak.account.profile.application.result.ProfileImageUploadResult;
import com.chalnakchalnak.account.util.PresignedUrlUtil;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@RequiredArgsConstructor
@Service
public class MinioProfileImageAdapter implements ProfileImageStoragePort {

  private final AmazonS3 storage;

  private final String algorithm = "AWS4-HMAC-SHA256";
  private final String service = "s3";

  @Value("${cloud.aws.s3.bucket}")
  private String bucket;

  @Value("${cloud.aws.region.static}")
  private String region;

  @Value("${cloud.aws.credentials.access-key}")
  private String accessKey;

  @Value("${cloud.aws.credentials.secret-key}")
  private String secretKey;

  @Value("${cloud.aws.s3.endpoint:}")
  private String endpoint;

  @Override
  public ProfileImageUploadResult generatePresignedPost(
      PrepareProfileImageCommand presignedUrlRequestDto) {
    // 날짜 형식
    Date now = new Date();
    SimpleDateFormat dateFmt = new SimpleDateFormat("yyyyMMdd");
    dateFmt.setTimeZone(TimeZone.getTimeZone("UTC"));
    String date = dateFmt.format(now);

    SimpleDateFormat dateTimeFmt = new SimpleDateFormat("yyyyMMdd'T'HHmmss'Z'");
    dateTimeFmt.setTimeZone(TimeZone.getTimeZone("UTC"));
    String dateTime = dateTimeFmt.format(now);

    // Credential
    String credential =
        this.accessKey + "/" + date + "/" + this.region + "/" + this.service + "/aws4_request";

    // Policy 문서 생성
    String policyDoc =
        PresignedUrlUtil.generateDoc(
            this.bucket,
            this.algorithm,
            credential,
            dateTime,
            presignedUrlRequestDto.getContentType());

    // Policy 인코딩
    Base64.Encoder encoder = Base64.getEncoder();
    String policy = encoder.encodeToString(policyDoc.getBytes(StandardCharsets.UTF_8));

    // 서명 키 생성
    byte[] signingKey =
        PresignedUrlUtil.getSignatureKey(this.secretKey, date, this.region, this.service);

    // Signature 생성
    String signature = PresignedUrlUtil.bytesToHex(PresignedUrlUtil.hmacSha256(signingKey, policy));

    Map<String, String> fields = new LinkedHashMap<>();
    fields.put("key", presignedUrlRequestDto.getKey());
    fields.put("Content-Type", presignedUrlRequestDto.getContentType());
    fields.put("bucket", this.bucket);
    fields.put("X-Amz-Algorithm", this.algorithm);
    fields.put("X-Amz-Credential", credential);
    fields.put("X-Amz-Date", dateTime);
    fields.put("Policy", policy);
    fields.put("X-Amz-Signature", signature);

    String url =
        endpoint == null || endpoint.isBlank()
            ? "https://" + this.bucket + ".s3." + this.region + ".amazonaws.com/"
            : UriComponentsBuilder.fromUriString(endpoint)
                .pathSegment(bucket)
                .path("/")
                .build()
                .toUriString();

    return ProfileImageUploadResult.builder().url(url).fields(fields).build();
  }

  @Override
  public ProfileImageContent read(String key) {
    try (var object = storage.getObject(bucket, key)) {
      String contentType = object.getObjectMetadata().getContentType();
      if (contentType == null
          || !Set.of("image/png", "image/jpeg", "image/webp", "image/bmp").contains(contentType)) {
        throw new org.springframework.web.server.ResponseStatusException(
            org.springframework.http.HttpStatus.BAD_REQUEST, "Invalid profile image type");
      }
      byte[] body = object.getObjectContent().readNBytes(10 * 1024 * 1024 + 1);
      if (body.length > 10 * 1024 * 1024) {
        throw new org.springframework.web.server.ResponseStatusException(
            org.springframework.http.HttpStatus.PAYLOAD_TOO_LARGE);
      }
      return new ProfileImageContent(body, contentType);
    } catch (AmazonS3Exception error) {
      throw new org.springframework.web.server.ResponseStatusException(
          error.getStatusCode() == 404
              ? org.springframework.http.HttpStatus.NOT_FOUND
              : org.springframework.http.HttpStatus.BAD_GATEWAY);
    } catch (java.io.IOException error) {
      throw new org.springframework.web.server.ResponseStatusException(
          org.springframework.http.HttpStatus.BAD_GATEWAY);
    }
  }
}
