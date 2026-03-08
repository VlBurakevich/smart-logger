package com.solution.coreservice.scheduler;

import com.solution.coreservice.entity.MonitoringTask;
import com.solution.coreservice.service.report.ReportOrchestrator;
import com.solution.coreservice.service.report.ReportProcessingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReportScheduler {
    private final ReportProcessingService reportProcessingService;
    private final ReportOrchestrator reportOrchestrator;

    @Value("${app.scheduler.report.batch-size:10}")
    private int batchSize;

    @Scheduled(fixedRateString = "${app.scheduler.report.rate-ms}")
    @SchedulerLock(
            name = "SnapshotScheduler_lock",
            lockAtMostFor = "10m",
            lockAtLeastFor = "30s"
    )
    public void scheduleReportProcessing() {
        List<MonitoringTask> tasks = reportProcessingService.captureTasks(batchSize);

        for (MonitoringTask task: tasks) {
            try {
                reportOrchestrator.processSnapshot(task);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                reportProcessingService.handleFailure(task);
            }
        }
    }
}
