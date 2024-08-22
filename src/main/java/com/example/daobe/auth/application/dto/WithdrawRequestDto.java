package com.example.daobe.auth.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record WithdrawRequestDto(
        @JsonProperty("reason") List<String> reasonTypeList,
        String detail
) {
}
