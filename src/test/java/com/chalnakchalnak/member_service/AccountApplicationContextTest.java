package com.chalnakchalnak.member_service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(properties = {
        "spring.datasource.url=jdbc:h2:mem:account;MODE=PostgreSQL;DB_CLOSE_DELAY=-1",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.datasource.username=sa",
        "spring.datasource.password=",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.data.redis.host=localhost",
        "spring.data.redis.port=6379",
        "spring.data.redis.password=",
        "spring.kafka.bootstrap-servers=localhost:9092",
        "spring.kafka.listener.auto-startup=false",
        "spring.kafka.producer.acks=1",
        "spring.kafka.producer.retries=0",
        "spring.kafka.producer.properties.request.timeout.ms=1000",
        "spring.kafka.producer.properties.delivery.timeout.ms=1000",
        "spring.kafka.producer.properties.max.block.ms=1000",
        "spring.kafka.consumer.group-id=account-context-test",
        "coolsms.api-key=test",
        "coolsms.secret-key=test",
        "coolsms.sender-phone-number=01000000000",
        "coolsms.domain=https://api.coolsms.co.kr",
        "JWT.secret-key=0123456789012345678901234567890123456789012345678901234567890123",
        "JWT.token.access-expire-time=600000",
        "JWT.token.refresh-expire-time=1200000",
        "feign.client.member-service.url=http://localhost",
        "cloud.aws.credentials.access-key=test",
        "cloud.aws.credentials.secret-key=test",
        "cloud.aws.region.static=ap-northeast-2",
        "cloud.aws.s3.bucket=test-bucket"
})
class AccountApplicationContextTest {

    @Autowired
    private ApplicationContext context;

    @Test
    void exposesAuthIdentityVerificationAndMemberControllersFromOneContext() {
        assertThat(context.containsBean("authController")).isTrue();
        assertThat(context.containsBean("identityVerificationController")).isTrue();
        assertThat(context.containsBean("memberController")).isTrue();
    }
}
