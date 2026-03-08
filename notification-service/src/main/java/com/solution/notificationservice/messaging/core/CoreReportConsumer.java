package com.solution.notificationservice.messaging.core;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.solution.notificationservice.dto.ReportResult;
import com.solution.notificationservice.service.NotificationService;
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
    private final NotificationService notificationService;

    @KafkaListener(
            topics = "${app.kafka.notification-report-result}",
            groupId = "inference-service-group",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void consume(String rawJsonMessage, Acknowledgment ack) {
        ReportResult reportResult = null;

        try {
            reportResult = objectMapper.readValue(rawJsonMessage, ReportResult.class);
            log.info(">>>> [KAFKA CONSUME REPORT]");

            notificationService.processReport(reportResult);

            ack.acknowledge();
        } catch (JsonProcessingException e) {
            log.error(">>>> [KAFKA ERROR] Failed to parse JSON. Poison pill skipped! Message: {}, Error: {}",
                    rawJsonMessage, e.getMessage());
            ack.acknowledge();
        } catch (Exception e) {
            log.error(">>>> [KAFKA ERROR] Business logic failed", e);
        }
    }
}
