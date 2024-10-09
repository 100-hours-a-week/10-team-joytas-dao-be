package com.example.daobe.common.outbox;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "outboxes")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Outbox {

    @Id
    @Column(name = "noti_outbox_id")
    private String id;

    @Column(name = "aggregate_type", nullable = false)
    private String aggregateType;

    @Column(columnDefinition = "TEXT")
    private String payload;

    @Column(name = "is_complete")
    private boolean isComplete;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Builder
    public Outbox(String id, String aggregateType, String payload) {
        this.id = id;
        this.aggregateType = aggregateType;
        this.payload = payload;
        this.isComplete = false;
        this.createdAt = LocalDateTime.now();
    }

    public void complete() {
        this.isComplete = true;
    }
}
