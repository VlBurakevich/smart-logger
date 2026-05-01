package com.solution.notificationservice.messaging.core;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.solution.notificationservice.dto.SnapshotAlert;
import com.solution.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CoreSnapshotConsumer {

    private final NotificationService notificationService;
    private final ObjectMapper objectMapper;

    @KafkaListener(
            topics = "${app.kafka.notification-snapshot-alert}",
            groupId = "notification-service-group",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void consume(String rawJsonMessage) {
        SnapshotAlert alert = null;

        try {
            alert = objectMapper.readValue(rawJsonMessage, SnapshotAlert.class);
            log.info(">>>> [KAFKA CONSUME ALERT] snapshotId {} ", alert.snapshotId());

            notificationService.processAlert(alert);

        } catch (JsonProcessingException e) {
            log.error(">>>> [KAFKA ERROR] Failed to parse JSON. Poison pill skipped! Message: {}. Error: {}",
                    rawJsonMessage, e.getMessage());
        } catch (Exception e) {
            log.error(">>>> [KAFKA ERROR] Business logic failed for Snapshot ID: {}",
                    (alert != null ? alert.snapshotId() : "unknown"), e);
            throw new RuntimeException(e);
        }
    }
}
