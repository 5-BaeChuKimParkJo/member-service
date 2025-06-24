package com.chalnakchalnak.member_service.client.feign.external.grade;

import feign.Feign;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GradeApiConfig {

    @Bean
    public GradeApi gradeApi(@Value("${external.grade-service.url}") String baseUrl) {
        return Feign.builder()
                .encoder(new JacksonEncoder())
                .decoder(new JacksonDecoder())
                .target(GradeApi.class, baseUrl);
    }
}
