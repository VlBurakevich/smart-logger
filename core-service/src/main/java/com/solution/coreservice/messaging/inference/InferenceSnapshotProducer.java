package com.solution.coreservice.messaging.inference;

import com.solution.coreservice.dto.messaging.InferenceSnapshotRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class InferenceSnapshotProducer {

    private final KafkaTemplate<String, InferenceSnapshotRequest> kafkaTemplate;

    @Value("${app.kafka.inference-request}")
    private String snapshotRequestTopic;

    public void sendToAnalyze(InferenceSnapshotRequest request) {
        log.info(">>>> [KAFKA] Send to topic: {}", snapshotRequestTopic);

        kafkaTemplate.send(snapshotRequestTopic, request.taskId().toString(), request)
                .whenComplete((result, error) -> {
                    if (error == null) {
                        log.info(">>>> [KAFKA SUCCESS] send success");
                    } else {
                        log.error(">>>> [KAFKA FAILURE] send failure: {}", error.getMessage());
                    }
                });
    }
}