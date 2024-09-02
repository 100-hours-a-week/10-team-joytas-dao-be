package com.example.daobe.notification.domain.convert;

import com.example.daobe.notification.domain.convert.dto.DomainInfo;

public interface DomainEventConvert {

    String supportNameToLowerCase();

    DomainInfo convertToDomainInfo(Long domainId);
}
