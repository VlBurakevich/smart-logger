package com.solution.coreservice.controller;

import com.solution.coreservice.dto.response.ReportResponse;
import com.solution.coreservice.dto.response.ReportShortResponse;
import com.solution.coreservice.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/reports")
public class ReportController {

    private final ReportService reportService;

    @GetMapping("/{reportId}")
    public ResponseEntity<ReportResponse> getReport(
            @RequestHeader("X-User-Id") UUID userId,
            @PathVariable String reportId
    ) {
        return ResponseEntity.ok(reportService.getReport(reportId, userId));
    }

    @GetMapping("/")
    public ResponseEntity<List<ReportShortResponse>> getAllReport(
            @RequestHeader("X-User-Id") UUID userId,
            Pageable pageable
    ) {
        return ResponseEntity.ok(reportService.getAllReport(pageable, userId));
    }

    @DeleteMapping("/{reportId}")
    public ResponseEntity<Void> deleteReportData(
            @RequestHeader("X-User-Id") UUID userId,
            @PathVariable String reportId
    ) {
        reportService.deleteReport(reportId, userId);
        return ResponseEntity.noContent().build();
    }
}
