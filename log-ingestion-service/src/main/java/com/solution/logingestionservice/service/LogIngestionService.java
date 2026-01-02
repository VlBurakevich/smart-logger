package com.solution.logingestionservice.service;


import com.solution.logingestionservice.dto.LogEventDto;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class LogIngestionService {
    private final KafkaTemplate<String, LogEventDto> kafkaTemplate;
    private final String topic;

    //TODO добавить логику проверки accountId
    // скорее всего подключить reddis проверять в начале в нём потом делать запрос в core

    public LogIngestionService(
            KafkaTemplate<String, LogEventDto> kafkaTemplate,
            @Value("${KAFKA_TOPIC_LOGS_RAW:logs.raw}") String topic
    ) {
        this.kafkaTemplate = kafkaTemplate;
        this.topic = topic;
    }

    public void sendToKafka(LogEventDto logEvent) {
        String key = logEvent.getAccountId();

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

    public void sendBatchToKafka(List<LogEventDto> batch) {
        for (LogEventDto logEvent : batch) {
            String key = logEvent.getAccountId();

            kafkaTemplate.send(topic, key, logEvent)
                .whenComplete((result, ex) -> {
                    if (ex != null) {
                        log.error("Failed to send log for account: {}", key);
                    }
                });
        }
    }
}
