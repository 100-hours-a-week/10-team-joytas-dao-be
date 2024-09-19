package com.example.daobe.user.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record UserWithdrawRequestDto(
        @JsonProperty("reason") List<String> reasonTypeList,
        String detail
) {
}
