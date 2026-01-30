package com.solution.coreservice.service;

import com.solution.coreservice.dto.response.SnapshotResponse;
import com.solution.coreservice.dto.response.SnapshotShortResponse;
import com.solution.coreservice.repository.SnapshotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SnapshotService {

    private final SnapshotRepository snapshotRepository;

    public SnapshotResponse get(UUID userId, UUID snapshotId) {
        return null;
    }

    public List<SnapshotShortResponse> getAll(UUID userId, Pageable pageable) {
        return null;
    }

    public void delete(UUID userId, String snapshotId) {

    }
}
