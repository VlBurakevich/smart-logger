package com.solution.coreservice.service;

import com.solution.coreservice.entity.OutboxMessage;
import com.solution.coreservice.entity.OutboxStatus;
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

    }

    @Transactional
    public void cleanupOldTasks() {
        OffsetDateTime daysAgo = OffsetDateTime.now(ZoneOffset.UTC).minusDays(3);
        int deletedCount = outboxRepository.deleteCompletedOlderThan(daysAgo);
        log.info("Cleaner: removed {} old outbox records", deletedCount);
    }
}
