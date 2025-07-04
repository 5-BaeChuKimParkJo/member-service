package com.chalnakchalnak.member_service.client.feign.external.grade;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "grade-service", path = "api/v1", url = "https://api.cabbage-secondhand.shop/grade-service/api/v1")
public interface GradeServiceFeignClient {

    @GetMapping("/default-grade")
    String getDefaultGradeUuid();
}