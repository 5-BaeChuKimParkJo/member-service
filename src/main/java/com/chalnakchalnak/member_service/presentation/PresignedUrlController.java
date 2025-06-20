package com.chalnakchalnak.member_service.presentation;

import com.chalnakchalnak.member_service.application.PresignedUrlService;
import com.chalnakchalnak.member_service.dto.in.PresignedUrlRequestDto;
import com.chalnakchalnak.member_service.dto.in.SaveImageUrlRequestDto;
import com.chalnakchalnak.member_service.vo.in.PresignedUrlRequestVo;
import com.chalnakchalnak.member_service.vo.out.PresignedUrlResponseVo;
import com.chalnakchalnak.member_service.vo.in.SaveImageUrlRequestVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@Slf4j
@Tag(name = "ProfileImage", description = "프로필 이미지 관련 API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/member")
public class PresignedUrlController {

    private final PresignedUrlService presignedUrlService;

    @Operation(summary = "AWS S3 Presigned url 요청")
    @PostMapping("/presigned-url")
    public PresignedUrlResponseVo getPresignedUrl(@RequestHeader("X-Member-Uuid") String memberUuid,
                                                  @RequestBody @Valid PresignedUrlRequestVo presignedUrlRequestVo) {

        return presignedUrlService
                .generatePresignedPost(PresignedUrlRequestDto
                        .toPresignedUrlRequestDto(presignedUrlRequestVo, memberUuid))
                .toVo();
    }

    @Operation(summary = "프로필 이미지 key DB저장")
    @PutMapping("/save-url")
    public void saveImageUrl(@RequestHeader("X-Member-Uuid") String memberUuid,
                             @RequestBody @Valid SaveImageUrlRequestVo saveImageUrlRequestVo) {
        presignedUrlService.saveImageUrl(SaveImageUrlRequestDto.from(saveImageUrlRequestVo, memberUuid));
    }
}
