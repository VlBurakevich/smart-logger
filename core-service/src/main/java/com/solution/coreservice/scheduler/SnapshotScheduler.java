package com.solution.coreservice.scheduler;

import com.solution.coreservice.entity.MonitoringTask;
import com.solution.coreservice.service.snapshot.SnapshotOrchestrator;
import com.solution.coreservice.service.snapshot.SnapshotPersistenceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SnapshotScheduler {
    private final SnapshotPersistenceService snapshotProcessingService;
    private final SnapshotOrchestrator snapshotOrchestrator;

    @Value("${app.scheduler.snapshot.batch-size:10}")
    private int batchSize;

    @Scheduled(fixedRateString = "${app.scheduler.snapshot.rate-ms}")
    @SchedulerLock(
            name = "SnapshotScheduler_lock",
            lockAtMostFor = "2m",
            lockAtLeastFor = "5s"
    )
    public void scheduleSnapshotProcessing() {
        List<MonitoringTask> tasks = snapshotProcessingService.captureTasks(batchSize);

        for (MonitoringTask task: tasks) {
            try {
                snapshotOrchestrator.processSnapshot(task);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                snapshotProcessingService.handleFailure(task);
            }
        }
    }
}
