package com.example.daobe.objet.application;

import com.example.daobe.chat.application.ChatService;
import com.example.daobe.chat.domain.ChatRoom;
import com.example.daobe.lounge.application.LoungeService;
import com.example.daobe.lounge.domain.Lounge;
import com.example.daobe.objet.application.dto.ObjetCreateRequestDto;
import com.example.daobe.objet.application.dto.ObjetDetailResponseDto;
import com.example.daobe.objet.application.dto.ObjetInfoResponseDto;
import com.example.daobe.objet.application.dto.ObjetMeResponseDto;
import com.example.daobe.objet.application.dto.ObjetResponseDto;
import com.example.daobe.objet.application.dto.ObjetUpdateRequestDto;
import com.example.daobe.objet.domain.Objet;
import com.example.daobe.objet.domain.ObjetSharer;
import com.example.daobe.user.application.UserService;
import com.example.daobe.user.domain.User;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ObjetFacadeService {

    private final UserService userService;
    private final LoungeService loungeService;
    private final ObjetService objetService;
    private final ObjetSharerService objetSharerService;
    private final ChatService chatRoomService;

    @Transactional
    public ObjetInfoResponseDto createObjet(ObjetCreateRequestDto request, Long userId) {
        Lounge lounge = loungeService.getLoungeById(request.loungeId());
        User findUser = userService.getUserById(userId);
        ChatRoom chatRoom = chatRoomService.createChatRoom();
        Objet createdObjet = objetService.createAndSaveObjet(request, findUser, lounge, chatRoom);
        objetSharerService.createAndSaveObjetSharer(request, userId, createdObjet);
        return ObjetInfoResponseDto.of(createdObjet);
    }

    @Transactional
    public ObjetInfoResponseDto updateObjet(ObjetUpdateRequestDto request, Long objetId, Long userId) {
        Objet updatedObjet = objetService.updateAndSaveObjet(request, objetId, userId);
        objetSharerService.updateObjetSharerList(updatedObjet, request.sharers());
        return ObjetInfoResponseDto.of(updatedObjet);
    }

    public List<ObjetResponseDto> getAllObjetsInLounge(Long userId, Long loungeId, boolean isSharer) {
        if (isSharer) {
            return objetService.getObjetListInLoungeOfSharer(userId, loungeId);
        }
        return objetService.getObjetListInLounge(loungeId);

    }

    public ObjetDetailResponseDto getObjetDetail(Long userId, Long objetId) {
        return objetService.getObjetDetailInfo(userId, objetId);
    }

    public List<ObjetMeResponseDto> getMyObjetList(Long userId) {
        List<ObjetSharer> objetSharerList = objetSharerService.getRecentObjetSharerList(userId);
        return objetSharerList.stream()
                .map((objetSharer) -> ObjetMeResponseDto.of(objetSharer.getObjet()))
                .toList();
    }

    @Transactional
    public ObjetInfoResponseDto deleteObjet(Long objetId, Long userId) {
        return objetService.delete(objetId, userId);
    }
}
