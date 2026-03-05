package com.solution.notificationservice.messaging.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CoreReportConsumer {

    private final ObjectMapper objectMapper;

    @KafkaListener(
            topics = "${app.kafka.notification-report-result}",
            groupId = "inference-service-group",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void consume(String rawJsonMessage, Acknowledgment ack) {

    }
}
