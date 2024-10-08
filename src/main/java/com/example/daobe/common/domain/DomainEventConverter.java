package com.example.daobe.common.domain;

import com.example.daobe.lounge.domain.event.LoungeInvitedEvent;
import com.example.daobe.objet.domain.event.ObjetInviteEvent;
import com.example.daobe.user.domain.event.UserPokeEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DomainEventConverter {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private static final Map<String, Class<? extends DomainEvent>> EVENT_TYPE_MAP = new HashMap<>();

    static {
        EVENT_TYPE_MAP.put("ObjetInviteEvent", ObjetInviteEvent.class);
        EVENT_TYPE_MAP.put("LoungeInvitedEvent", LoungeInvitedEvent.class);
        EVENT_TYPE_MAP.put("UserPokeEvent", UserPokeEvent.class);
    }

    public DomainEvent convert(String eventType, String payload) {
        Class<? extends DomainEvent> eventClass = EVENT_TYPE_MAP.get(eventType);
        if (eventClass == null) {
            throw new IllegalArgumentException(eventType + " 는 존재하지 하지 않는 이벤트 타입입니다.");
        }
        return deserialize(payload, eventClass);
    }

    private DomainEvent deserialize(String payload, Class<? extends DomainEvent> eventClass) {
        try {
            return objectMapper.readValue(payload, eventClass);
        } catch (IOException ex) {
            throw new RuntimeException("역직렬화에 실패했습니다.", ex);
        }
    }
}
