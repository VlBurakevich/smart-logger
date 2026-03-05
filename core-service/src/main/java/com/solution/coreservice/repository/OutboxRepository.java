package com.solution.coreservice.repository;

import com.solution.coreservice.entity.OutboxMessage;
import com.solution.coreservice.entity.OutboxStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public interface OutboxRepository extends JpaRepository<OutboxMessage, UUID> {

    @Query(value = """
            UPDATE outbox_messages
            SET status = 'PROCESSING', updated_at = NOW()
            WHERE id IN (
                SELECT id FROM outbox_messages
                WHERE status = 'PENDING'
                ORDER BY created_at ASC
                LIMIT :batchSize
                FOR UPDATE SKIP LOCKED
            )
            RETURNING *
            """, nativeQuery = true)
    List<OutboxMessage> grabMessageForProcessing(@Param("batchSize") int batchSize);

    @Modifying
    @Query("""
            UPDATE OutboxMessage m
            SET m.status = :status, m.errorLog = :error, m.updatedAt = CURRENT_TIMESTAMP
            WHERE m.id = :id
            """)
    void updateStatus(@Param("id") UUID id,
                      @Param("status") OutboxStatus status,
                      @Param("error") String error);


    @Modifying
    @Query(value = "DELETE FROM outbox_messages " +
            "WHERE status = 'SENT'" +
            "AND updated_at < :beforeDate", nativeQuery = true)
    int deleteCompletedOlderThan(@Param("beforeDate") OffsetDateTime daysAgo);

    @Modifying
    @Query(value = """
            UPDATE outbox_messages
            SET status = 'PENDING',
                retry_count = retry_count + 1,
                error_log = 'Task was stuck in PROCESSING and was reset',
                updated_at = NOW()
            WHERE id IN (
                SELECT id FROM outbox_messages
                WHERE status = 'PROCESSING'
                    AND updated_at < NOW() - INTERVAL '5 minutes'
                LIMIT :batchSize
                FOR UPDATE SKIP LOCKED
            )
            """, nativeQuery = true)
    void resetStuckTasks(@Param("batchSize") Integer batchSize);
}
