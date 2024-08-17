package com.example.daobe.myroom.presentation;

import com.example.daobe.common.response.ApiResponse;
import com.example.daobe.myroom.application.MyRoomService;
import com.example.daobe.myroom.application.dto.MyRoomInfoResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/rooms")
@RequiredArgsConstructor
public class MyRoomController {

    private final MyRoomService myRoomService;

    @GetMapping
    public ResponseEntity<ApiResponse<MyRoomInfoResponseDto>> getUserMyRoomInfo(
            @RequestParam("user_id") Long userId
    ) {
        return ResponseEntity.ok(new ApiResponse<>(
                "ROOM_INFO_LOADED_SUCCESS",
                myRoomService.getMyRoomInfo(userId)
        ));
    }
}
