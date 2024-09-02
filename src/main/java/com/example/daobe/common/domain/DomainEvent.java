package com.example.daobe.common.domain;

public interface DomainEvent {

    Long getDomainId();

    Long getSendUserId();

    Long getReceiveUserId();
}
