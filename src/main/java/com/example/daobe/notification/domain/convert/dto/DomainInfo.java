package com.example.daobe.notification.domain.convert.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record DomainInfo(
        @JsonProperty("domain_id") Long domainId,
        String name
) {
}
