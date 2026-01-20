package com.solution.coreservice.service;

import com.solution.coreservice.dto.request.LogEventRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class LogProcessingService {
    @Value("${KAFKA_TOPIC_OUTPUT_LOG:logs.processing}")
    private String outputTopic;

    private final KafkaTemplate<String, LogEventRequest> kafkaTemplate;

    public LogProcessingService(KafkaTemplate<String, LogEventRequest> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }
//
//    @KafkaListener(
//        topics = "${KAFKA_TOPIC_INPUT_LOG:logs.raw}",
//        groupId = "core-service-group",
//        concurrency = "3",
//        containerFactory = "kafkaListenerContainerFactory"
//    )
//    public void processLog(
//        @Payload LogEventDto logEvent,
//        @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
//        @Header(KafkaHeaders.OFFSET) long offset,
//        Acknowledgment ack
//    ) {
//        try {
//
//        }
//    }

}
