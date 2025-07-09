package com.chalnakchalnak.member_service.kafka.consumer;

import com.chalnakchalnak.member_service.application.MemberService;
import com.chalnakchalnak.member_service.common.entity.BaseResponseStatus;
import com.chalnakchalnak.member_service.common.exception.BaseException;
import com.chalnakchalnak.member_service.dto.in.GradeEventRequestDto;
import com.chalnakchalnak.member_service.dto.in.MemberUpdateRequestDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class GradeEventConsumer {

    private final MemberService memberService;
    private final ObjectMapper objectMapper;

    @KafkaListener(
            topics = "grade-service.grade-history",
            groupId = "grade-group-1"
    )
    public void consume(List<String> messages) throws JsonProcessingException {

        for(String message : messages) {

            GradeEventRequestDto gradeEventRequestDto
                    = objectMapper.readValue(message, GradeEventRequestDto.class);

            log.error("gradeEventRequestDto {}", gradeEventRequestDto.toString());

            MemberUpdateRequestDto memberUpdateRequestDto = MemberUpdateRequestDto.builder()
                    .memberUuid(gradeEventRequestDto.getMemberUuid())
                    .point(gradeEventRequestDto.getPoint())
                    .gradeUuid(gradeEventRequestDto.getGradeUuid())
                    .build();

            memberService.updateDynamic(memberUpdateRequestDto);
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