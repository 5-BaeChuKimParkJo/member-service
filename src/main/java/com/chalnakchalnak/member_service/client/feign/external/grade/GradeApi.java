package com.chalnakchalnak.member_service.client.feign.external.grade;

import feign.RequestLine;

public interface GradeApi {

    @RequestLine("GET /defaultUuid")
    String getDefaultGradeUuid();
}