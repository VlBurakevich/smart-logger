package com.solution.coreservice.repository;

import com.solution.coreservice.dto.response.ServiceNamesResponse;
import com.solution.coreservice.entity.MonitoringTask;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MonitoringTaskRepository extends JpaRepository<MonitoringTask, UUID> {

    Optional<MonitoringTask> findByIdAndApiKey_User_Id(UUID id, UUID userId);

    boolean existsByIdAndApiKey_User_Id(UUID id, UUID userId);

    boolean existsByApiKey_User_Id(UUID userId);

    Page<MonitoringTask> findAllByApiKey_User_Id(UUID userId, Pageable pageable);

    @Query(value = """
            SELECT * FROM monitoring_tasks
            WHERE current_snapshot_id IS NULL
                AND (last_snapshot_at IS NULL
                    OR last_snapshot_at <= (NOW() - (snapshot_interval_sec * interval '1 second') + interval '500 milliseconds'))
            ORDER BY last_snapshot_at ASC NULLS FIRST
            FOR UPDATE SKIP LOCKED
            LIMIT :batchSize
            """, nativeQuery = true)
    List<MonitoringTask> findReadyForSnapshot(@Param("batchSize") int batchSize);

    @Query("SELECT DISTINCT m.serviceName FROM MonitoringTask m WHERE m.apiKey.user.id = :userId")
    List<String> findServiceNamesByUserId(@Param("userId") UUID userId);
}
