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
import jakarta.persistence.PreUpdate;
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

    @Column(name = "snapshot_start_time", nullable = false)
    private OffsetDateTime snapshotStartTime;

    @Column(name = "snapshot_end_time")
    private OffsetDateTime snapshotEndTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    @JdbcType(PostgreSQLEnumJdbcType.class)
    private SnapshotStatus status = SnapshotStatus.PENDING;

    @Column(name = "errors", columnDefinition = "JSONB")
    @JdbcTypeCode(SqlTypes.JSON)
    private JsonNode errors;

    @Column(name = "max_severity")
    private Double maxSeverity;

    @Column(name = "root_cause")
    private String rootCause;

    @Column(name = "suggested_action")
    private String suggestedAction;

    @Column(name = "ai_description", columnDefinition = "TEXT")
    private String aiDescription;

    @Column(name = "ai_score")
    private Double aiScore;

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

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
                ", snapshotStartTime=" + snapshotStartTime +
                ", snapshotEndTime=" + snapshotEndTime +
                ", status=" + status +
                ", errors=" + errors +
                ", maxSeverity=" + maxSeverity +
                ", rootCase=" + rootCause +
                ", suggestedAction=" + suggestedAction +
                ", aiDescription=" + aiDescription +
                ", aiScore=" + aiScore +
                ",  updatedAt=" + updatedAt +
                ", createdAt=" + createdAt +
                "}";
    }

    @PrePersist
    protected void onCreate()  {
        this.createdAt = OffsetDateTime.now(ZoneOffset.UTC);
        this.updatedAt = OffsetDateTime.now(ZoneOffset.UTC);
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = OffsetDateTime.now(ZoneOffset.UTC);
    }


}
