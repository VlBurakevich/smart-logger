package com.solution.modelinferenceservice.messaging;

import com.solution.modelinferenceservice.dto.InferenceSnapshotResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ResultSnapshotProducer {

    private final KafkaTemplate<String, InferenceSnapshotResult> kafkaTemplate;

    @Value("${app.kafka.inference-result}")
    private String snapshotResultTopic;

    public void send(InferenceSnapshotResult inferenceResult) {
        log.info(">>>> [KAFKA] Send to topic: {}", snapshotResultTopic);

        kafkaTemplate.send(snapshotResultTopic, inferenceResult.taskId(), inferenceResult)
                .whenComplete((result, error) -> {
                    if (error == null) {
                        log.info(">>>> [KAFKA SUCCESS] send success");
                    } else {
                        log.error(">>>> [KAFKA FAILURE] send failure: {}", error.getMessage());
                    }
                });
    }
}
