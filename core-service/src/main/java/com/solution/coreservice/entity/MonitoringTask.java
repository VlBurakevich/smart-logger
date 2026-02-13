package com.solution.coreservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcType;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.dialect.PostgreSQLEnumJdbcType;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

@Entity
@Table(
        name = "monitoring_tasks",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_key_service_name",
                        columnNames = {"api_key_id", "service_name"}
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MonitoringTask {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "api_key_id", nullable = false)
    private ApiKey apiKey;

    @Column(name = "service_name", length = 100, nullable = false)
    private String serviceName;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", columnDefinition = "task_status", nullable = false)
    @JdbcType(PostgreSQLEnumJdbcType.class)
    private TaskStatus status = TaskStatus.READY;

    @Column(name = "check_sec")
    private Integer checkSec;

    @Column(name = "report_hr")
    private Integer reportHr;

    @Column(name = "last_checked_at")
    private OffsetDateTime lastCheckedAt;

    @Column(name = "last_report_at")
    private OffsetDateTime lastReportAt;

    @Column(name = "created_at", updatable = false, nullable = false)
    private OffsetDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    @Version
    @Column(name = "version")
    private Integer version;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MonitoringTask that = (MonitoringTask) o;
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
                ", serviceName=" + serviceName +
                ", status=" + status +
                ", checkMin=" + checkSec +
                ", reportHr=" + reportHr +
                ", lastCheckedAt=" + lastCheckedAt +
                ", lastReportAt=" + lastReportAt +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                "}";
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = OffsetDateTime.now(ZoneOffset.UTC);
        this.updatedAt = OffsetDateTime.now(ZoneOffset.UTC);
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = OffsetDateTime.now(ZoneOffset.UTC);
    }
}
