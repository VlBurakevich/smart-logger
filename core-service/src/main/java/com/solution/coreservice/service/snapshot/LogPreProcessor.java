package com.solution.coreservice.service.snapshot;

import com.solution.coreservice.dto.messaging.LogEntry;
import com.solution.coreservice.entity.MonitoringTask;
import com.solution.coreservice.repository.SnapshotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LogPreProcessor {

    private final SnapshotRepository snapshotRepository;
    private final SnapshotPersistenceService snapshotPersistenceService;

    @Transactional
    public String performLocalAnalyze(MonitoringTask task, List<LogEntry> logs) {

        return "LocalAnalyze";
    }

    public boolean isSimpleEnoughFromLocalAnalyze(List<LogEntry> logs) {
        return logs == null || logs.isEmpty();
    }
}
