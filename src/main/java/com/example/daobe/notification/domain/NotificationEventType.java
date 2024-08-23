package com.example.daobe.notification.domain;

import static com.example.daobe.notification.exception.NotificationExceptionType.NON_MATCH_DOMAIN_EVENT_TYPE;

import com.example.daobe.common.domain.DomainEvent;
import com.example.daobe.lounge.domain.event.LoungeInviteEvent;
import com.example.daobe.notification.exception.NotificationException;
import java.util.stream.Stream;

public enum NotificationEventType {
    LOUNGE_INVITE_EVENT("N0001", LoungeInviteEvent.class),
    ;

    private final String type;
    private final Class<? extends DomainEvent> eventClass;

    NotificationEventType(String type, Class<? extends DomainEvent> eventClass) {
        this.type = type;
        this.eventClass = eventClass;
    }

    public String type() {
        return type;
    }

    public static String getEventTypeByDomainEvent(DomainEvent domainEvent) {
        return Stream.of(NotificationEventType.values())
                .filter(eventType -> eventType.eventClass.equals(domainEvent.getClass()))
                .findFirst()
                .orElseThrow(() -> new NotificationException(NON_MATCH_DOMAIN_EVENT_TYPE))
                .toString();
    }
}
