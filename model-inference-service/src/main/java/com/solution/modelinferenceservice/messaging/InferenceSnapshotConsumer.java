package com.solution.modelinferenceservice.messaging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.solution.modelinferenceservice.dto.InferenceSnapshotRequest;
import com.solution.modelinferenceservice.dto.InferenceSnapshotResult;
import com.solution.modelinferenceservice.service.LogAnalysisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class InferenceSnapshotConsumer {

    private final LogAnalysisService logAnalysisService;
    private final ResultSnapshotProducer producer;
    private final ObjectMapper objectMapper;

    @KafkaListener(
            topics = "${app.kafka.inference-request}",
            groupId = "inference-service-group",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void consume(String rawJsonMessage, Acknowledgment ack) {
        InferenceSnapshotRequest request = null;

        try {
            request = objectMapper.readValue(rawJsonMessage, InferenceSnapshotRequest.class);
            log.info(">>>> [KAFKA CONSUME] Received snapshot for Snapshot ID: {}. Logs count: {}",
                    request.snapshotId(), request.logs().size());

            InferenceSnapshotResult result = logAnalysisService.analyzeLogs(request);
            producer.send(result);

            ack.acknowledge();

        } catch (JsonProcessingException e) {
            log.error(">>>> [KAFKA ERROR] Failed to parse JSON. Poison pill skipped! Message: {}. Error: {}",
                    rawJsonMessage, e.getMessage());

            ack.acknowledge();
        } catch (Exception e) {
            log.error(">>>> [KAFKA ERROR] Business logic failed for Snapshot ID: {}",
                    (request != null ? request.snapshotId() : "unknown"), e);
        }
    }
}
