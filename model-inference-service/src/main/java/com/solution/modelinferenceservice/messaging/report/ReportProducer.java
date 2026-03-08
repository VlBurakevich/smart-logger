package com.solution.modelinferenceservice.messaging.report;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.solution.modelinferenceservice.dto.InferenceReportResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReportProducer {

    @Value("")
    private String reportResultTopic;

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public void send(InferenceReportResult inferenceReportResult) {
        log.info(">>>> [KAFKA] Send to topic: {}", reportResultTopic);
        String jsonPayload;

        try {
            jsonPayload = objectMapper.writeValueAsString(inferenceReportResult);
        } catch (JsonProcessingException e) {
            log.error("Ошибка конвертации в JSON: {}", e.getMessage());
            return;
        }

        kafkaTemplate.send(reportResultTopic, reportResultTopic., jsonPayload)
                .whenComplete((result, error) -> {
                    if (error == null) {
                        log.info(">>>> [KAFKA SUCCESS] send success");
                    } else {
                        log.error(">>>> [KAFKA FAILURE] send failure: {}", error.getMessage());
                    }
                });
    }
}
