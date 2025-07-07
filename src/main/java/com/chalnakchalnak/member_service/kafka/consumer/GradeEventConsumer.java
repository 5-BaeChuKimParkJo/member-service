package com.chalnakchalnak.member_service.kafka.consumer;

import com.chalnakchalnak.member_service.application.MemberService;
import com.chalnakchalnak.member_service.common.entity.BaseResponseStatus;
import com.chalnakchalnak.member_service.common.exception.BaseException;
import com.chalnakchalnak.member_service.dto.in.GradeEventRequestDto;
import com.chalnakchalnak.member_service.dto.in.MemberUpdateRequestDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class GradeEventConsumer {

    private final MemberService memberService;
    private final ObjectMapper objectMapper;

    @KafkaListener(
            topics = "grade-topic",
            groupId = "grade-group-1"
    )
    public void consume(ConsumerRecord<String, String> record) {
        log.info("Received message: {}", record.value());

        try {
            // 예시: JSON 파싱 → DTO → 서비스 처리
            GradeEventRequestDto event = parseEvent(record.value());

            MemberUpdateRequestDto memberUpdateRequestDto = MemberUpdateRequestDto.builder()
                    .memberUuid(event.getMemberUuid())
                    .point(event.getPoint())
                    .gradeUuid(event.getGradeUuid())
                    .build();

            memberService.updateDynamic(memberUpdateRequestDto);
        } catch (Exception e) {
            log.error("Failed to process grade event: {}", record.value(), e);
        }
    }

    private GradeEventRequestDto parseEvent(String json) {
        try {
            return objectMapper.readValue(json, GradeEventRequestDto.class);
        }catch (Exception e) {
            throw new BaseException(BaseResponseStatus.FAILED_TO_READ_GRADE_EVENT);
        }
    }
}