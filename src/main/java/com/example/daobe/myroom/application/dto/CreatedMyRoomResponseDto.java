package com.example.daobe.myroom.application.dto;

import com.example.daobe.myroom.domain.MyRoom;

public record CreatedMyRoomResponseDto(
        Long myRoomId
) {

    public static CreatedMyRoomResponseDto of(MyRoom newMyRoom) {
        return new CreatedMyRoomResponseDto(newMyRoom.getId());
    }
}
