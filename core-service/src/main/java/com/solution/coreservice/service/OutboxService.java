package com.solution.coreservice.service;

import com.solution.coreservice.entity.OutboxMessage;
import com.solution.coreservice.entity.OutboxStatus;
import com.solution.coreservice.repository.OutboxRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

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
}
