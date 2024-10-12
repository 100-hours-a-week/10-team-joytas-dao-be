package com.example.daobe.myroom.presentation;

import com.example.daobe.myroom.application.MyRoomService;
import com.example.daobe.myroom.application.dto.CreatedMyRoomRequestDto;
import com.example.daobe.myroom.application.dto.CreatedMyRoomResponseDto;
import com.example.daobe.myroom.application.dto.MyRoomInfoResponseDto;
import com.example.daobe.myroom.application.dto.UpdateMyRoomRequestDto;
import com.example.daobe.myroom.application.dto.UpdateMyRoomResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/rooms")
@RequiredArgsConstructor
public class MyRoomController {

    private final MyRoomService myRoomService;

    @GetMapping
    public ResponseEntity<MyRoomInfoResponseDto> getUserMyRoomInfo(
            @RequestParam("user_id") Long userId
    ) {
        return ResponseEntity.ok(myRoomService.getMyRoomInfo(userId));
    }

    @PostMapping
    public ResponseEntity<CreatedMyRoomResponseDto> generatedNewMyRoom(
            @AuthenticationPrincipal Long userId,
            @RequestBody CreatedMyRoomRequestDto request
    ) {
        return ResponseEntity.ok(myRoomService.generatedMyRoom(userId, request));
    }

    @PatchMapping("/{myRoomId}")
    public ResponseEntity<UpdateMyRoomResponseDto> updateMyRoomInfo(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long myRoomId,
            @RequestBody UpdateMyRoomRequestDto request
    ) {
        return ResponseEntity.ok(myRoomService.updateMyRoomInfo(userId, myRoomId, request));
    }
}
