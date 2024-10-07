package com.example.daobe.auth.application.dto;

import com.example.daobe.user.application.dto.UserWithdrawRequestDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

@Deprecated(since = "2024-09-18")
public record WithdrawRequestDto(
        @JsonProperty("reason") List<String> reasonTypeList,
        String detail
) {

    public UserWithdrawRequestDto toUserWithdrawRequestDto() {
        return new UserWithdrawRequestDto(reasonTypeList, detail);
    }
}
