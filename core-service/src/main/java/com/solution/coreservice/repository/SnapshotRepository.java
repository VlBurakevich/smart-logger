package com.solution.coreservice.repository;

import com.solution.coreservice.entity.Snapshot;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface SnapshotRepository extends JpaRepository<Snapshot, UUID> {
    Optional<Snapshot> findByIdAndMonitoringTask_ApiKey_User_Id(UUID snapshotId, UUID userId);

    Page<Snapshot> findAllByMonitoringTask_ApiKey_User_Id(UUID snapshotId, Pageable pageable);

    boolean existsByIdAndMonitoringTask_ApiKey_User_Id(UUID snapshotId, UUID userId);
}
