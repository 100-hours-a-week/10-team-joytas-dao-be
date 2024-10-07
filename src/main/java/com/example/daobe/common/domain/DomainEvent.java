package com.example.daobe.common.domain;

public interface DomainEvent {

    String getEventId();

    Long getDomainId();

    Long getSendUserId();

    Long getReceiveUserId();
}
