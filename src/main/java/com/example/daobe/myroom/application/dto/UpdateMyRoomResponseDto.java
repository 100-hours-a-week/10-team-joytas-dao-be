package com.example.daobe.myroom.application.dto;

import com.example.daobe.myroom.domain.MyRoom;

public record UpdateMyRoomResponseDto(
        Long myRoomId
) {

    public static UpdateMyRoomResponseDto of(MyRoom myRoom) {
        return new UpdateMyRoomResponseDto(myRoom.getId());
    }
}
