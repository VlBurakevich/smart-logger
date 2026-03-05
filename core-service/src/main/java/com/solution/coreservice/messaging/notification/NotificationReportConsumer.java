package com.solution.coreservice.messaging.notification;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.solution.coreservice.dto.messaging.ReportRequest;
import com.solution.coreservice.service.report.ReportProcessingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationReportConsumer {
    private final ObjectMapper objectMapper;
    private final ReportProcessingService reportProcessingService;

    @KafkaListener(
            topics = "${app.kafka.notification-report-request}",
            groupId = "notification-service-group",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void consume(String rawJsonMessage, Acknowledgment ack) {
        ReportRequest request = null;

        try {
            request = objectMapper.readValue(rawJsonMessage, ReportRequest.class);
            log.info(">>>> [KAFKA CONSUME] Received report for user {}", request.userId());
            reportProcessingService.startRequestReport(request);

            ack.acknowledge();
        } catch (JsonProcessingException e) {
            log.error(">>>> [KAFKA ERROR] Failed to parse JSON. Poison pill skipped! Message: {}. Error: {}",
                    rawJsonMessage, e.getMessage());

            ack.acknowledge();
        } catch (Exception e) {
            log.error(">>>> [KAFKA ERROR] Business logic failed for User ID: {}",
                    (request != null ? request.userId() : "unknown"), e);
            ack.acknowledge();
        }
    }
}
