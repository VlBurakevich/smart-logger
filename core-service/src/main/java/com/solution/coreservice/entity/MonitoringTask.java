package com.solution.coreservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    @Column(name = "snapshot_sec")
    private Integer snapshotSec;

    @Column(name = "report_hr")
    private Integer reportHr;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "current_snapshot_id")
    private Snapshot currentSnapshot;

    @Column(name = "last_snapshot_at")
    private OffsetDateTime lastSnapshotAt;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "current_report_id")
    private Report report;

    @Column(name = "last_report_at")
    private OffsetDateTime lastReportAt;

    @Column(name = "created_at", updatable = false, nullable = false)
    private OffsetDateTime createdAt;

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
                ", snapshotSec=" + snapshotSec +
                ", reportHr=" + reportHr +
                ", lastCheckedAt=" + lastSnapshotAt +
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
