package com.example.daobe.notification.domain.convert;

import com.example.daobe.notification.domain.NotificationEventType;
import com.example.daobe.notification.domain.convert.dto.DomainInfo;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class DomainEventConvertMapper {

    private final Map<String, DomainEventConvert> convertMap;

    public DomainEventConvertMapper(List<DomainEventConvert> convertList) {
        convertMap = new HashMap<>();
        convertList.forEach(convert -> convertMap.put(convert.supportNameToLowerCase(), convert));
    }

    public DomainInfo convert(NotificationEventType eventType, Long domainId) {
        String className = eventType.getClassName();
        DomainEventConvert converter = convertMap.get(className);
        if (converter == null) {
            throw new RuntimeException("일치하는 도메인 이벤트가 없습니다.");
        }
        return converter.convertToDomainInfo(domainId);
    }
}
