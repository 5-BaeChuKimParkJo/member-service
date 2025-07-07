package com.chalnakchalnak.member_service.kafka.produce;

import com.chalnakchalnak.member_service.common.entity.BaseResponseStatus;
import com.chalnakchalnak.member_service.common.exception.BaseException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class DLQProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    private static final String DLQ_TOPIC = "grade-topic-dlq";

    public void sendToDLQ(String originalMessage, String reason) {
        try {
            String dlqMessage = String.format("{\"message\": %s, \"reason\": \"%s\"}", originalMessage, reason);
            kafkaTemplate.send(DLQ_TOPIC, dlqMessage);
            log.warn("Sent to DLQ: {}", dlqMessage);
        } catch (Exception e) {
            throw new BaseException(BaseResponseStatus.FAILED_SEND_MESSAGE_TO_DLQ);
        }
    }
}
