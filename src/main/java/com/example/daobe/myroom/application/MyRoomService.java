package com.example.daobe.myroom.application;

import static com.example.daobe.myroom.exception.MyRoomExceptionType.ALREADY_EXIST_MY_ROOM;
import static com.example.daobe.myroom.exception.MyRoomExceptionType.NOT_EXIST_MY_ROOM;
import static com.example.daobe.user.exception.UserExceptionType.NOT_EXIST_USER;

import com.example.daobe.myroom.application.dto.CreatedMyRoomRequestDto;
import com.example.daobe.myroom.application.dto.CreatedMyRoomResponseDto;
import com.example.daobe.myroom.application.dto.MyRoomInfoResponseDto;
import com.example.daobe.myroom.application.dto.UpdateMyRoomRequestDto;
import com.example.daobe.myroom.application.dto.UpdateMyRoomResponseDto;
import com.example.daobe.myroom.domain.MyRoom;
import com.example.daobe.myroom.domain.repository.MyRoomRepository;
import com.example.daobe.myroom.exception.MyRoomException;
import com.example.daobe.user.domain.User;
import com.example.daobe.user.domain.repository.UserRepository;
import com.example.daobe.user.exception.UserException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MyRoomService {

    private final MyRoomRepository myRoomRepository;
    private final UserRepository userRepository;

    public MyRoomInfoResponseDto getMyRoomInfo(Long userId) {
        MyRoom findMyRoom = myRoomRepository.findByUserId(userId)
                .orElseThrow(() -> new MyRoomException(NOT_EXIST_MY_ROOM));
        return MyRoomInfoResponseDto.of(findMyRoom);
    }

    @Transactional
    public CreatedMyRoomResponseDto generatedMyRoom(Long userId, CreatedMyRoomRequestDto request) {
        myRoomRepository.findByUserId(userId).ifPresent(a -> {
            throw new MyRoomException(ALREADY_EXIST_MY_ROOM);
        });

        User findUser = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(NOT_EXIST_USER));

        MyRoom newMyRoom = MyRoom.builder()
                .user(findUser)
                .type(request.type())
                .build();

        myRoomRepository.save(newMyRoom);
        return CreatedMyRoomResponseDto.of(newMyRoom);
    }

    @Transactional
    public UpdateMyRoomResponseDto updateMyRoomInfo(
            Long userId, Long myRoomId, UpdateMyRoomRequestDto request
    ) {
        MyRoom findMyRoom = myRoomRepository.findById(myRoomId)
                .orElseThrow(() -> new UserException(NOT_EXIST_USER));
        findMyRoom.isMatchOwnerOrThrow(userId);

        findMyRoom.updatedName(request.roomName());
        myRoomRepository.save(findMyRoom);

        return UpdateMyRoomResponseDto.of(findMyRoom);
    }
}
