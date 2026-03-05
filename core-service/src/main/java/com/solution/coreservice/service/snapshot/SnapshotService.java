package com.solution.coreservice.service.snapshot;

import com.solution.coreservice.dto.response.SnapshotResponse;
import com.solution.coreservice.dto.response.SnapshotShortResponse;
import com.solution.coreservice.entity.Snapshot;
import com.solution.coreservice.exception.ServiceException;
import com.solution.coreservice.mapper.SnapshotMapper;
import com.solution.coreservice.repository.SnapshotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SnapshotService {
    private final SnapshotRepository snapshotRepository;
    private final SnapshotMapper snapshotMapper;

    public SnapshotResponse get(UUID userId, UUID snapshotId) {
        return snapshotRepository.findByIdAndMonitoringTask_ApiKey_User_Id(snapshotId, userId)
                .map(snapshotMapper::toResponse)
                .orElseThrow(() -> new ServiceException(HttpStatus.NOT_FOUND, "Snapshot not found"));
    }

    public Page<SnapshotShortResponse> getAll(UUID userId, Pageable pageable) {
        Page<Snapshot> snapshots = snapshotRepository.findAllByMonitoringTask_ApiKey_User_Id(userId, pageable);
        return snapshots.map(snapshotMapper::toShortResponse);
    }

    @Transactional
    public void delete(UUID userId, UUID snapshotId) {
        if (!snapshotRepository.existsByIdAndMonitoringTask_ApiKey_User_Id(snapshotId, userId)) {
            throw new ServiceException(HttpStatus.NOT_FOUND, "Snapshot not found");
        }
        snapshotRepository.deleteById(snapshotId);
    }
}
