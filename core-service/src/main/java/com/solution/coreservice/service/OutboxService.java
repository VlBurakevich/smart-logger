package com.solution.coreservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.solution.coreservice.entity.OutboxMessage;
import com.solution.coreservice.entity.OutboxStatus;
import com.solution.coreservice.exception.OutboxException;
import com.solution.coreservice.repository.OutboxRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class OutboxService {
    private final OutboxRepository outboxRepository;
    private final ObjectMapper objectMapper;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void updateFinalStatus(UUID id, OutboxStatus outboxStatus, String message) {
        outboxRepository.updateStatus(id, outboxStatus, message);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public List<OutboxMessage> grabMessageForProcessing(int batchSize) {
        return outboxRepository.grabMessageForProcessing(batchSize);
    }

    @Transactional
    public void resetStuckTasks(Integer batchSize) {
        outboxRepository.resetStuckTasks(batchSize);
    }

    @Transactional
    public void cleanupOldTasks() {
        OffsetDateTime daysAgo = OffsetDateTime.now(ZoneOffset.UTC).minusDays(3);
        int deletedCount = outboxRepository.deleteCompletedOlderThan(daysAgo);
        log.info("Cleaner: removed {} old outbox records", deletedCount);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void saveEvent(String topic, Object payload) {
        try {
            OutboxMessage message = new OutboxMessage();
            message.setTopic(topic);
            message.setPayload(objectMapper.valueToTree(payload));
            message.setStatus(OutboxStatus.PENDING);
            message.setCreatedAt(OffsetDateTime.now(ZoneOffset.UTC));

            outboxRepository.save(message);
        } catch (Exception e) {
            log.error("Failed to save outbox event for the topic: {}", topic, e);
            throw new OutboxException("Critical failure: could not persist outboxMessage");
        }
    }
}
