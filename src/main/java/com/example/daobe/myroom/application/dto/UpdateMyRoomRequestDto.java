package com.example.daobe.myroom.application.dto;

import com.example.daobe.myroom.domain.MyRoom;

public record UpdateMyRoomRequestDto(
        String roomName
) {

    public static UpdateMyRoomRequestDto of(MyRoom myRoom) {
        return new UpdateMyRoomRequestDto(myRoom.getName());
    }
}
