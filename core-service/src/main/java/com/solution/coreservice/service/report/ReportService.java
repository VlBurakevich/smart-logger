package com.solution.coreservice.service.report;

import com.solution.coreservice.dto.response.ReportResponse;
import com.solution.coreservice.dto.response.ReportShortResponse;
import com.solution.coreservice.entity.Report;
import com.solution.coreservice.exception.ServiceException;
import com.solution.coreservice.mapper.ReportMapper;
import com.solution.coreservice.repository.ReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReportService {
    private final ReportRepository reportRepository;
    private final ReportMapper reportMapper;

    public ReportResponse getReport(UUID reportId, UUID userId) {
        return reportRepository.findByIdAndMonitoringTask_ApiKey_User_Id(reportId, userId)
                .map(reportMapper::toResponse)
                .orElseThrow(() -> new ServiceException(HttpStatus.NOT_FOUND, "Report not found"));
    }

    public Page<ReportShortResponse> getAllReport(Pageable pageable, UUID userId) {
        Page<Report> reports = reportRepository.findAllByMonitoringTask_ApiKey_User_Id(userId, pageable);
        return reports.map(reportMapper::toShortResponse);
    }

    @Transactional
    public void deleteReport(UUID reportId, UUID userId) {
        if (!reportRepository.existsByIdAndMonitoringTask_ApiKey_User_Id(reportId, userId)) {
            throw new ServiceException(HttpStatus.NOT_FOUND, "Report not found");
        }
        reportRepository.deleteById(reportId);
    }
}
