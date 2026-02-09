package com.solution.coreservice.messaging.inference;

import com.solution.coreservice.dto.messaging.InferenceRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InferenceProducer {

    private final KafkaTemplate<String, InferenceRequest> inferenceProducer;


    public void sendToSnapshot(InferenceRequest request) {

    }
}