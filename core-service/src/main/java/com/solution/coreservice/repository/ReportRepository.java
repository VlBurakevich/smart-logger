package com.solution.coreservice.repository;

import com.solution.coreservice.entity.Report;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ReportRepository extends JpaRepository<Report, UUID> {

    Optional<Report> findByIdAndMonitoringTask_ApiKey_User_Id(UUID reportId, UUID userId);

    Page<Report> findAllByMonitoringTask_ApiKey_User_Id(UUID userId, Pageable pageable);

    boolean existsByIdAndMonitoringTask_ApiKey_User_Id(UUID reportId, UUID userId);
}
