package com.example.daobe.myroom.application;

import com.example.daobe.myroom.application.dto.MyRoomInfoResponseDto;
import com.example.daobe.myroom.domain.MyRoom;
import com.example.daobe.myroom.domain.repository.MyRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MyRoomService {

    private final MyRoomRepository myRoomRepository;

    public MyRoomInfoResponseDto getMyRoomInfo(Long userId) {
        MyRoom findMyRoom = myRoomRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 마이룸 입니다"));
        return MyRoomInfoResponseDto.of(findMyRoom);
    }
}
