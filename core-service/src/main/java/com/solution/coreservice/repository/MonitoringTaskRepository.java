package com.solution.coreservice.repository;

import com.solution.coreservice.entity.MonitoringTask;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MonitoringTaskRepository extends JpaRepository<MonitoringTask, UUID> {

    //TODO как вариант перенести query функции в entityManager

    @Query(
            value = """
                        UPDATE monitoring_tasks
                        SET status = 'PROCESSING'::task_status
                        WHERE id IN (
                            SELECT id FROM monitoring_tasks
                            WHERE status = 'READY'::task_status
                                AND (last_checked_at is NULL OR (last_checked_at + (check_sec * interval '1 second')) <= :now)
                            ORDER BY last_checked_at ASC NULLS FIRST
                            LIMIT :batchSize
                            FOR UPDATE SKIP LOCKED
                        )
                        RETURNING *
                    """, nativeQuery = true
    )
    List<MonitoringTask> findReadyToSnapshot(@Param("now") OffsetDateTime now, @Param("batchSize") int batchSize);

    @Query(
            value = """
                        UPDATE monitoring_tasks
                        SET status = 'PROCESSING'::task_status
                        WHERE id IN (
                            SELECT id FROM monitoring_tasks
                            WHERE status = 'READY'::task_status
                                AND (last_report_at is NULL OR (last_report_at + (report_hr * interval '1 hour')) <= :now)
                            ORDER BY last_report_at ASC NULLS FIRST
                            LIMIT :batchSize
                            FOR UPDATE SKIP LOCKED
                        )
                        RETURNING *
                    """, nativeQuery = true
    )
    List<MonitoringTask> findReadyToReport(@Param("now")OffsetDateTime now,@Param("batchSize") int batchSize);

    Optional<MonitoringTask> findByIdAndApiKey_User_Id(UUID id, UUID userId);

    boolean existsByIdAndApiKey_User_Id(UUID id, UUID userId);

    Page<MonitoringTask> findAllByApiKey_User_Id(UUID userId, Pageable pageable);
}
