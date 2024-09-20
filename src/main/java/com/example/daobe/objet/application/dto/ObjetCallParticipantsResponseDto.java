package com.example.daobe.objet.application.dto;

public record ObjetCallParticipantsResponseDto(
        Long callingUserNum
) {

    public static ObjetCallParticipantsResponseDto of(Long objetid) {
        return new ObjetCallParticipantsResponseDto(objetid);
    }
}
