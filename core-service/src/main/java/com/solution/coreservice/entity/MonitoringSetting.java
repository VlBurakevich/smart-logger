package com.solution.coreservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(
        name = "monitoring_settings",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_monitoring_account_service",
                        columnNames = {"account_id", "service_name"}
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MonitoringSetting {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name = "api_key", nullable = false)
    private UUID apiKey;

    @Column(name = "service_name", length = 100, nullable = false)
    private String serviceName;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @Column(name = "check_min")
    private Integer checkMin;

    @Column(name = "report_hr")
    private Integer reportHr;

    @Column(name = "last_checked_at")
    private OffsetDateTime lastCheckedAt;

    @Column(name = "last_report_at")
    private OffsetDateTime lastReportAt;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false, nullable = false)
    private OffsetDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MonitoringSetting that = (MonitoringSetting) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "MonitoringSetting{" +
                "id=" + id +
                ", userId=" + userId +
                ", apiKey=" + apiKey +
                ", serviceName=" + serviceName +
                ", isActive=" + isActive +
                ", checkMin=" + checkMin +
                ", reportHr=" + reportHr +
                ", lastCheckedAt=" + lastCheckedAt +
                ", lastReportAt=" + lastReportAt +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                "}";
    }
}
