CREATE TABLE IF NOT EXISTS outboxs
(
    noti_outbox_id VARCHAR(128) NOT NULL,
    aggregate_type VARCHAR(255) NOT NULL,
    payload        TEXT         NOT NULL,
    is_complete    TINYINT      NOT NULL,
    created_at     DATETIME(6)  NOT NULL,
    PRIMARY KEY (noti_outbox_id)
) ENGINE = InnoDB;
