package com.solution.coreservice.entity;

public enum OutboxStatus {
    PENDING,
    PROCESSING,
    SENT,
    FAILED
}
