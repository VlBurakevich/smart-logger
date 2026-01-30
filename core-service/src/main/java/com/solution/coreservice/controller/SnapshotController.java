package com.solution.coreservice.controller;

import com.solution.coreservice.dto.response.SnapshotResponse;
import com.solution.coreservice.dto.response.SnapshotShortResponse;
import com.solution.coreservice.service.SnapshotService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/snapshots")
public class SnapshotController {

    private final SnapshotService snapshotService;

    @GetMapping("/{snapshotId}")
    public ResponseEntity<SnapshotResponse> get(
            @RequestHeader UUID userId,
            @PathVariable UUID snapshotId
    ) {
        return ResponseEntity.ok(snapshotService.get(userId, snapshotId));
    }

    @GetMapping
    public ResponseEntity<List<SnapshotShortResponse>> getAll(
            @RequestHeader UUID userId,
            Pageable pageable
    ) {
        return ResponseEntity.ok(snapshotService.getAll(userId, pageable));
    }

    @DeleteMapping("/{snapshotId}")
    public ResponseEntity<Void> delete(
            @RequestHeader UUID userId,
            @PathVariable String snapshotId
    ) {
        snapshotService.delete(userId, snapshotId);
        return ResponseEntity.noContent().build();
    }
}
