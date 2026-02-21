package com.solution.coreservice.scheduler;

import com.solution.coreservice.entity.MonitoringTask;
import com.solution.coreservice.service.SnapshotPersistenceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SnapshotScheduler {
    private final SnapshotPersistenceService processingService;

    @Value("${app.scheduler.snapshot.batch-size:10}")
    private int batchSize;

    @Scheduled(fixedRateString = "${app.scheduler.snapshot.rate-ms}")
    public void scheduleSnapshotProcessing() {
        List<MonitoringTask> tasks = processingService.captureTasks(batchSize);

        for (MonitoringTask task: tasks) {
            try {
                processingService.processSnapshot(task);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                processingService.handleFailure(task);
            }
        }
    }
}
