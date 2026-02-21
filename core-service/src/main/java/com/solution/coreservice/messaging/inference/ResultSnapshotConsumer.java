package com.solution.coreservice.messaging.inference;

import com.solution.coreservice.dto.messaging.InferenceSnapshotResponse;
import com.solution.coreservice.service.SnapshotPersistenceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ResultSnapshotConsumer {

    private final SnapshotPersistenceService snapshotPersistenceService;

    @KafkaListener(
            topics = "${app.kafka.inference-result}",
            groupId = "inference-service-group",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void consume(InferenceSnapshotResponse response, Acknowledgment ack) {
        log.info(">>>> [KAFKA CONSUME] Received snapshot for Task ID: {}.",
                response.snapshotId());

        try {
            snapshotPersistenceService.complete(response);

            ack.acknowledge();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
}
