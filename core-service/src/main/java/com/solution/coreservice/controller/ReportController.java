package com.solution.coreservice.controller;

import com.solution.coreservice.dto.response.ReportResponse;
import com.solution.coreservice.dto.response.ReportShortResponse;
import com.solution.coreservice.service.report.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/core/reports")
public class ReportController {
    private final ReportService reportService;

    @GetMapping("/{id}")
    public ResponseEntity<ReportResponse> getReport(
            @RequestHeader("User-Id") UUID userId,
            @PathVariable UUID id
    ) {
        return ResponseEntity.ok(reportService.getReport(id, userId));
    }

    @GetMapping("/")
    public ResponseEntity<Page<ReportShortResponse>> getAllReport(
            @RequestHeader("User-Id") UUID userId,
            Pageable pageable
    ) {
        return ResponseEntity.ok(reportService.getAllReport(pageable, userId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReportData(
            @RequestHeader("User-Id") UUID userId,
            @PathVariable UUID id
    ) {
        reportService.deleteReport(id, userId);
        return ResponseEntity.noContent().build();
    }
}
