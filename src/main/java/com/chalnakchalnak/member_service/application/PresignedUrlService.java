package com.chalnakchalnak.member_service.application;

import com.chalnakchalnak.member_service.dto.in.PresignedUrlRequestDto;
import com.chalnakchalnak.member_service.dto.in.SaveImageUrlRequestDto;
import com.chalnakchalnak.member_service.dto.out.PresignedUrlResponseDto;

public interface PresignedUrlService {

    PresignedUrlResponseDto generatePresignedUrl(PresignedUrlRequestDto presignedUrlRequestDto);

    void saveImageUrl(SaveImageUrlRequestDto saveImageUrlRequestDto);
}

