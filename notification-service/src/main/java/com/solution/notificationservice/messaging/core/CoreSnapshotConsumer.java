package com.solution.notificationservice.messaging.core;

import com.solution.notificationservice.dto.NotificationSnapshotRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CoreSnapshotConsumer {


    @KafkaListener(
            topics = "${app.kafka.notification-snapshot}",
            groupId = "notification-service-group",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void consume(NotificationSnapshotRequest request, Acknowledgment ack) {
        log.info(">>>> [KAFKA CONSUME] ");

        try {

            ack.acknowledge();
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

}
