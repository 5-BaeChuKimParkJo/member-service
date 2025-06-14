package com.chalnakchalnak.member_service.application;

import com.chalnakchalnak.member_service.common.entity.BaseResponseStatus;
import com.chalnakchalnak.member_service.common.exception.BaseException;
import com.chalnakchalnak.member_service.dto.in.PresignedUrlRequestDto;
import com.chalnakchalnak.member_service.dto.in.SaveImageUrlRequestDto;
import com.chalnakchalnak.member_service.dto.out.PresignedUrlResponseDto;
import com.chalnakchalnak.member_service.entity.Member;
import com.chalnakchalnak.member_service.infrastructure.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

@Slf4j
@RequiredArgsConstructor
@Service
public class PresignedUrlServiceImpl implements PresignedUrlService{

    private final MemberRepository memberRepository;
    private final S3Presigner s3Presigner;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.region.static}")
    private String region;

    @Override
    public PresignedUrlResponseDto generatePresignedUrl(PresignedUrlRequestDto presignedUrlRequestDto) {
        PutObjectRequest objectRequest = PutObjectRequest.builder()
                .bucket(bucket)
                .key(presignedUrlRequestDto.getFileName())
                .contentType(presignedUrlRequestDto.getContentType())
                .build();

        PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(5)) // 5분 유효
                .putObjectRequest(objectRequest)
                .build();

        PresignedPutObjectRequest presignedRequest = s3Presigner.presignPutObject(presignRequest);

        String presignedUrl = presignedRequest.url().toString();
        String uploadFileUrl = "https://" + bucket + ".s3." + region + ".amazonaws.com/"
                + URLEncoder.encode(presignedUrlRequestDto.getFileName(), StandardCharsets.UTF_8);

        return PresignedUrlResponseDto.builder()
                .presignedUrl(presignedUrl)
                .uploadFileUrl(uploadFileUrl)
                .build();
    }

    @Override
    @Transactional
    public void saveImageUrl(SaveImageUrlRequestDto saveImageUrlRequestDto) {
        Member member = memberRepository.findByMemberUuid(saveImageUrlRequestDto.getMemberUuid())
                .orElseThrow(() -> new BaseException(BaseResponseStatus.NO_EXISTS_MEMBER));
        member.setProfileImageUrl(saveImageUrlRequestDto.getUploadFileUrl());
    }
}
