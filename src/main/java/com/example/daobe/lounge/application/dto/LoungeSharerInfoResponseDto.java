package com.example.daobe.lounge.application.dto;

import com.example.daobe.lounge.domain.LoungeSharer;
import com.example.daobe.user.domain.User;
import java.util.List;

public record LoungeSharerInfoResponseDto(
        Long userId,
        String profileUrl,
        String nickname
) {

    public static LoungeSharerInfoResponseDto of(LoungeSharer loungeSharer) {
        User user = loungeSharer.getUser();

        return new LoungeSharerInfoResponseDto(
                user.getId(),
                user.getProfileUrl(),
                user.getNickname()
        );
    }

    public static List<LoungeSharerInfoResponseDto> of(List<LoungeSharer> loungeSharerList) {
        return loungeSharerList.stream()
                .map(LoungeSharerInfoResponseDto::of)
                .toList();
    }
}
