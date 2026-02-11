package com.solution.modelinferenceservice.messaging;

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

    @KafkaListener(
            topics = "${app.kafka.inference-result}",
            groupId = "inference-service-group",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void consume(InferenceSnapshotRequest request, Acknowledgment ack) {
        log.info(">>>> [KAFKA CONSUME] Received snapshot for Task ID: {}. Logs count: {}",
                request.taskId(), request.logs().size());

        try {
            InferenceSnapshotResult result = logAnalysisService.analyzeLogs(request);

            producer.send(result);

            ack.acknowledge();
        } catch (Error e) {
            log.error(e.getMessage());
        }
    }
}
