package com.solution.notificationservice.messaging.core;

import com.solution.notificationservice.dto.CoreReportRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CoreReportProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Value("${app.kafka.notification-report-request}")
    private String notificationRequestTopic;

    public void requestReport(CoreReportRequest request) {
        log.info(">>>> [KAFKA] Send to topic: {}", notificationRequestTopic);

        kafkaTemplate.send(notificationRequestTopic, request.userId().toString(), request.toString())
                .whenComplete((result, error) -> {
                    if (error == null) {
                        log.info(">>>> [KAFKA SUCCESS] send success");
                    } else {
                        log.error(">>>> [KAFKA FAILURE] send failure: {}", error.getMessage());
                    }
                });
    }
}
