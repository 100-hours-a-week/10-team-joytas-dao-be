package com.example.daobe.objet.application;

import com.example.daobe.chat.application.ChatService;
import com.example.daobe.chat.domain.ChatRoom;
import com.example.daobe.lounge.application.LoungeService;
import com.example.daobe.lounge.domain.Lounge;
import com.example.daobe.objet.application.dto.ObjetCreateRequestDto;
import com.example.daobe.objet.application.dto.ObjetCreateResponseDto;
import com.example.daobe.objet.application.dto.ObjetDetailInfoDto;
import com.example.daobe.objet.application.dto.ObjetInfoDto;
import com.example.daobe.objet.application.dto.ObjetMeInfoDto;
import com.example.daobe.objet.application.dto.ObjetUpdateRequestDto;
import com.example.daobe.objet.domain.Objet;
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

    // 오브제 생성
    @Transactional
    public ObjetCreateResponseDto createObjet(ObjetCreateRequestDto request, Long userId) {
        Lounge lounge = loungeService.getLoungeById(request.loungeId());
        User findUser = userService.getUserById(userId);
        ChatRoom chatRoom = chatRoomService.createChatRoom();
        Objet createdObjet = objetService.createAndSaveObjet(request, findUser, lounge, chatRoom);
        objetSharerService.createAndSaveObjetSharer(request, findUser, createdObjet);
        return ObjetCreateResponseDto.of(createdObjet);
    }

    // 오브제 수정
    @Transactional
    public ObjetCreateResponseDto updateObjet(ObjetUpdateRequestDto request, Long objetId, Long userId) {
        User findUser = userService.getUserById(userId);
        Objet findObjet = objetService.getObjetById(objetId);
        Objet updatedObjet = objetService.updateAndSaveObjet(request, findObjet, userId);
        objetSharerService.updateAndSaveObjetSharer(request, findUser, updatedObjet);
        return ObjetCreateResponseDto.of(updatedObjet);
    }

    // 오브제 목록 조회
    public List<ObjetInfoDto> getAllObjetsInLounge(Long userId, Long loungeId, Boolean sharer) {

        if (Boolean.TRUE.equals(sharer)) {
            return objetService.getObjetListInLoungeOfSharer(userId, loungeId, sharer);
        } else {
            return objetService.getObjetListInLounge(loungeId);
        }
    }

    // 오브제 상세 조회
    public ObjetDetailInfoDto getObjetDetail(Long objetId) {
        Objet findObjet = objetService.getObjetById(objetId);
        return objetService.getObjetDetailInfo(findObjet);
    }

    // 유저 오브제 조회
    public List<ObjetMeInfoDto> getMyObjetList(Long userId) {
        return objetService.getMyRecentObjetList(userId);
    }

    // 오브제 삭제
    @Transactional
    public ObjetCreateResponseDto deleteObjet(Long objetId, Long userId) {
        Objet findObjet = objetService.getObjetById(objetId);
        return objetService.delete(findObjet, userId);
    }
}
