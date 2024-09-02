package com.example.daobe.myroom.application.dto;

import com.example.daobe.myroom.domain.MyRoom;

public record MyRoomInfoResponseDto(
        Long myRoomId,
        String type,
        String myRoomName
) {

    public static MyRoomInfoResponseDto of(MyRoom myRoom) {
        return new MyRoomInfoResponseDto(
                myRoom.getId(),
                myRoom.getType(),
                myRoom.getName()
        );
    }
}
