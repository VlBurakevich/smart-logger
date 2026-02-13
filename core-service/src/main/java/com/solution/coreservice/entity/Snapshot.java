package com.solution.coreservice.entity;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcType;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.dialect.PostgreSQLEnumJdbcType;
import org.hibernate.type.SqlTypes;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

@Entity
@Table(name = "snapshots",
    indexes = {
        @Index(
                name = "idx_snapshots_account_time",
                columnList = "monitoring_setting_id, created_at DESC"
        )
    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Snapshot {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "monitoring_task_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private MonitoringTask monitoringTask;

    @Column(name = "snapshot_time", nullable = false)
    private OffsetDateTime snapshotTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    @JdbcType(PostgreSQLEnumJdbcType.class)
    private SnapshotStatus status = SnapshotStatus.PENDING;

    @Column(name = "errors", columnDefinition = "JSONB")
    @JdbcTypeCode(SqlTypes.JSON)
    private JsonNode errors;

    @Column(name = "max_severity")
    private Double maxSeverity;

    @Column(name = "anomalies", columnDefinition = "JSONB")
    @JdbcTypeCode(SqlTypes.JSON)
    private JsonNode anomalies;

    @Column(name = "ai_description", columnDefinition = "TEXT")
    private String aiDescription;

    @Column(name = "ai_score")
    private Double aiScore;

    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Snapshot that = (Snapshot) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "Snapshot{" +
                "id=" + id +
                ", snapshotTime=" + snapshotTime +
                ", status=" + status +
                ", errors=" + errors +
                ", maxSeverity=" + maxSeverity +
                ", anomalies=" + anomalies +
                ", aiDescription=" + aiDescription +
                ", aiScore=" + aiScore +
                ", createdAt=" + createdAt +
                "}";
    }

    @PrePersist
    protected void onCreate()  {
        this.createdAt = OffsetDateTime.now(ZoneOffset.UTC);
    }
}
