ALTER TABLE monitoring_tasks
    ADD CONSTRAINT fk_mt_current_snapshot
    FOREIGN KEY (current_snapshot_id) REFERENCES snapshots(id)
    ON DELETE SET NULL;

ALTER TABLE monitoring_tasks
    ADD CONSTRAINT fk_mt_current_report
    FOREIGN KEY (current_report_id) REFERENCES reports(id)
    ON DELETE SET NULL;