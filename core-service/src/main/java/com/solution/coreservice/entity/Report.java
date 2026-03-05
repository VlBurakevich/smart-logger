package com.solution.coreservice.entity;

import jakarta.persistence.*;
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
import java.util.Map;
import java.util.UUID;

@Entity
@Table(name = "reports")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "monitoring_task_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private MonitoringTask monitoringTask;

    @Column(name = "report_start_time", nullable = false)
    private OffsetDateTime reportStartTime;

    @Column(name = "report_end_time", nullable = false)
    private OffsetDateTime reportEndTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    @JdbcType(PostgreSQLEnumJdbcType.class)
    private ReportStatus status = ReportStatus.PENDING;

    @Column(name = "summary", columnDefinition = "TEXT")
    private String summary;

    @Column(name = "log_level_count")
    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, Integer> logLevelCount;

    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;

    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Report that = (Report) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "Report{" +
                "id=" + id +
                ", reportStartTime=" + reportStartTime +
                ", reportEndTime=" + reportEndTime +
                ", status=" + status +
                ", summary=" +  summary +
                ", logLevelCount=" + logLevelCount +
                ", updatedAt=" + updatedAt +
                ", createdAt=" + createdAt +
                "}";
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = OffsetDateTime.now(ZoneOffset.UTC);
    }
}
