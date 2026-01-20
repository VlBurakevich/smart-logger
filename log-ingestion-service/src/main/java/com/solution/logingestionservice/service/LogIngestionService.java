package com.solution.logingestionservice.service;


import com.solution.logingestionservice.dto.LogEventRequest;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class LogIngestionService {
    private final KafkaTemplate<String, LogEventRequest> kafkaTemplate;
    private final String topic;

    public LogIngestionService(
            KafkaTemplate<String, LogEventRequest> kafkaTemplate,
            @Value("${KAFKA_TOPIC_LOGS_RAW:logs.raw}") String topic
    ) {
        this.kafkaTemplate = kafkaTemplate;
        this.topic = topic;
    }

    public void sendToKafka(LogEventRequest logEvent) {
        String key = logEvent.getApiKey();

        kafkaTemplate.send(topic, key, logEvent)
            .whenComplete(
                (result, ex) -> {
                    if (ex == null) {
                        log.info("Log sent successfully: {}", logEvent);
                    } else {
                        log.error("Log sent failed: {}", ex.getMessage());
                    }
                }
            );
    }

    public void sendBatchToKafka(List<LogEventRequest> batch) {
        for (LogEventRequest logEvent : batch) {
            String key = logEvent.getApiKey();

            kafkaTemplate.send(topic, key, logEvent)
                .whenComplete((result, ex) -> {
                    if (ex != null) {
                        log.error("Failed to send log for account: {}", key);
                    }
                });
        }
    }
}
