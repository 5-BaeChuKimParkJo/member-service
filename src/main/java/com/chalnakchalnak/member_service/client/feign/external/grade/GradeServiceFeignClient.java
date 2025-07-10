package com.chalnakchalnak.member_service.client.feign.external.grade;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(
        name = "grade-service",
        url = "https://api.cabbage-secondhand.shop", // <- 여기에만 도메인 지정
        path = "/grade-service/api/v1" // <- 경로는 여기에
)
public interface GradeServiceFeignClient {

    @GetMapping("/default-grade")
    String getDefaultGradeUuid();
}