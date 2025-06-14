package com.chalnakchalnak.member_service.presentation;

import com.chalnakchalnak.member_service.application.PresignedUrlService;
import com.chalnakchalnak.member_service.dto.in.PresignedUrlRequestDto;
import com.chalnakchalnak.member_service.dto.in.SaveImageUrlRequestDto;
import com.chalnakchalnak.member_service.vo.PresignedUrlRequestVo;
import com.chalnakchalnak.member_service.vo.PresignedUrlResponseVo;
import com.chalnakchalnak.member_service.vo.SaveImageUrlRequestVo;
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
@RequestMapping("/api/v1/member")
public class PresignedUrlController {

    private final PresignedUrlService presignedUrlService;

    @Operation(summary = "AWS S3 Presigned url 요청")
    @PostMapping("/presigned-url")
    public PresignedUrlResponseVo getPresignedUrl(@Valid @RequestBody PresignedUrlRequestVo presignedUrlRequestVo) {
        return presignedUrlService
                .generatePresignedUrl(PresignedUrlRequestDto.toPresignedUrlRequestDto(presignedUrlRequestVo))
                .toVo();
    }

    @Operation(summary = "이미지 url DB저장")
    @PutMapping("/save-url")
    public void saveImageUrl(@RequestHeader("memberUuid") String memberUuid,
                             @Valid @RequestBody SaveImageUrlRequestVo saveImageUrlRequestVo) {
        presignedUrlService.saveImageUrl(SaveImageUrlRequestDto.from(saveImageUrlRequestVo, memberUuid));
    }
}
