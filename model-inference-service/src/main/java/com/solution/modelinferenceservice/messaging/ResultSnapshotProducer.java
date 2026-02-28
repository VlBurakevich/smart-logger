package com.solution.modelinferenceservice.messaging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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

    @Value("${app.kafka.inference-result}")
    private String snapshotResultTopic;

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;


    public void send(InferenceSnapshotResult inferenceResult) {
        log.info(">>>> [KAFKA] Send to topic: {}", snapshotResultTopic);
        String jsonPayload;

        try {
            jsonPayload = objectMapper.writeValueAsString(inferenceResult);
        } catch (JsonProcessingException e) {
            log.error("Ошибка конвертации в JSON: {}", e.getMessage());
            return;
        }

        kafkaTemplate.send(snapshotResultTopic, inferenceResult.snapshotId().toString(), jsonPayload)
                .whenComplete((result, error) -> {
                    if (error == null) {
                        log.info(">>>> [KAFKA SUCCESS] send success");
                    } else {
                        log.error(">>>> [KAFKA FAILURE] send failure: {}", error.getMessage());
                    }
                });
    }
}
