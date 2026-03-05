package com.solution.coreservice.service.report;

import com.solution.coreservice.dto.messaging.ReportRequest;
import com.solution.coreservice.entity.MonitoringTask;
import com.solution.coreservice.repository.ReportRepository;
import com.solution.coreservice.repository.SnapshotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReportProcessingService {

    private final SnapshotRepository snapshotRepository;
    private final ReportRepository reportRepository;

    public void initializeReport(MonitoringTask task) {

    }

    public void finalizeReport() {

    }

    public void startRequestReport(ReportRequest request) {

    }

    public void finalizeRequestReport() {

    }
}
