package com.solution.coreservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "log_monitoring_settings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LogMonitoringSetting {

    @Id
    private UUID accountId;

    @Column(name = "service_name", length = 100,  nullable = false)
    private String serviceName;

    @Column(name = "check_min")
    private Integer checkMin;

    @Column(name = "report_hr")
    private Integer reportHr;

    @Column(name = "last_checked_at")
    private LocalDateTime lastCheckedAt;

    @Column(name = "last_report_at")
    private LocalDateTime lastReportAt;

    @Column(name = "active", nullable = false)
    private Boolean active = true;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false, nullable = false)
    private LocalDateTime createdAt;

    @CreationTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}
