package com.chalnakchalnak.account.application;

import com.chalnakchalnak.account.dto.in.PresignedUrlRequestDto;
import com.chalnakchalnak.account.dto.in.SaveImageUrlRequestDto;
import com.chalnakchalnak.account.dto.out.PresignedUrlResponseDto;

public interface PresignedUrlService {

    PresignedUrlResponseDto generatePresignedPost(PresignedUrlRequestDto presignedUrlRequestDto);

    void saveImageUrl(SaveImageUrlRequestDto saveImageUrlRequestDto);
}

