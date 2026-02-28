package com.solution.coreservice.controller;

import com.solution.coreservice.dto.response.SnapshotResponse;
import com.solution.coreservice.dto.response.SnapshotShortResponse;
import com.solution.coreservice.service.snapshot.SnapshotService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/core/snapshots")
public class SnapshotController {
    private final SnapshotService snapshotService;

    @GetMapping("/{id}")
    public ResponseEntity<SnapshotResponse> get(
            @RequestHeader UUID userId,
            @PathVariable UUID id
    ) {
        return ResponseEntity.ok(snapshotService.get(userId, id));
    }

    @GetMapping
    public ResponseEntity<Page<SnapshotShortResponse>> getAll(
            @RequestHeader UUID userId,
            Pageable pageable
    ) {
        return ResponseEntity.ok(snapshotService.getAll(userId, pageable));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @RequestHeader UUID userId,
            @PathVariable UUID id
    ) {
        snapshotService.delete(userId, id);
        return ResponseEntity.noContent().build();
    }
}
