package com.solution.coreservice.messaging.inference;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.solution.coreservice.dto.messaging.InferenceSnapshotResponse;
import com.solution.coreservice.service.snapshot.SnapshotPersistenceService;
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
    private final ObjectMapper objectMapper;

    @KafkaListener(
            topics = "${app.kafka.inference-result}",
            groupId = "inference-service-group",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void consume(String rawJsonMessage, Acknowledgment ack) {
        InferenceSnapshotResponse response = null;

        try {
            response = objectMapper.readValue(rawJsonMessage, InferenceSnapshotResponse.class);

            log.info(">>>> [KAFKA CONSUME] Received snapshot for Task ID: {}.",
                    response.snapshotId());

            snapshotPersistenceService.complete(response);

            ack.acknowledge();

        } catch (JsonProcessingException e) {
            log.error(">>>> [KAFKA ERROR] Failed to parse JSON. Poison pill skipped! Message: {}. Error: {}",
                    rawJsonMessage, e.getMessage());

            ack.acknowledge();
        } catch (Exception e) {
            log.error(">>>> [KAFKA ERROR] Business logic failed for Snapshot ID: {}",
                    (response != null ? response.snapshotId() : "unknown"), e);
            ack.acknowledge();
        }
    }
}
