package com.solution.coreservice.service;

import com.solution.coreservice.dto.response.ReportResponse;
import com.solution.coreservice.dto.response.ReportShortResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReportService {

    public ReportResponse getReport(String reportId, UUID userId) {
        return null;
    }

    public List<ReportShortResponse> getAllReport(Pageable pageable, UUID userId) {
        return null;
    }

    public void deleteReport(String reportId, UUID userId) {

    }
}
