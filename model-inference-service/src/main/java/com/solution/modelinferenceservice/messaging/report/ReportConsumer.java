package com.solution.modelinferenceservice.messaging.report;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.solution.modelinferenceservice.service.LogAnalysisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReportConsumer {
    private final LogAnalysisService logAnalysisService;
    private final ReportProducer reportProducer;
    private final ObjectMapper objectMapper;

    @KafkaListener(
            topics = "",
            groupId = "",
            containerFactory = ""
    )
    public void consume(String rawJsonMessage, Acknowledgment ack) {

    }
}
