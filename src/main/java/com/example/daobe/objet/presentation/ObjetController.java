package com.example.daobe.objet.presentation;

import com.example.daobe.objet.application.ObjetService;
import com.example.daobe.objet.application.ObjetSharerService;
import com.example.daobe.objet.application.dto.ObjetCreateRequestDto;
import com.example.daobe.objet.application.dto.ObjetDeleteResponseDto;
import com.example.daobe.objet.application.dto.ObjetDetailResponseDto;
import com.example.daobe.objet.application.dto.ObjetInfoResponseDto;
import com.example.daobe.objet.application.dto.ObjetMeResponseDto;
import com.example.daobe.objet.application.dto.ObjetSharerResponseDto;
import com.example.daobe.objet.application.dto.ObjetUpdateRequestDto;
import com.example.daobe.objet.application.dto.PagedObjetResponseDto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/objets")
@RequiredArgsConstructor
@Slf4j
public class ObjetController {

    private final ObjetService objetService;
    private final ObjetSharerService objetSharerService;

    @PostMapping
    public ResponseEntity<ObjetInfoResponseDto> generateObjet(
            @AuthenticationPrincipal Long userId,
            @RequestBody ObjetCreateRequestDto request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(objetService.createNewObjet(request, userId));
    }

    @GetMapping
    public ResponseEntity<PagedObjetResponseDto> getAllObjets(
            @AuthenticationPrincipal Long userId,
            @RequestParam("lounge_id") Long loungeId,
            @RequestParam(value = "cursor", required = false) Long cursor,
            @RequestParam(value = "is_owner", required = false, defaultValue = "false") boolean isOwner
    ) {
        PagedObjetResponseDto responseDto = isOwner ?
                objetService.getObjetListByUserId(userId, loungeId, cursor) :
                objetService.getObjetList(loungeId, cursor);
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/{objetId}")
    public ResponseEntity<ObjetDetailResponseDto> getObjetDetail(
            @PathVariable(name = "objetId") Long objetId
    ) {
        return ResponseEntity.ok(objetService.getObjetDetail(objetId));
    }

    @GetMapping("/{objetId}/sharers")
    public ResponseEntity<ObjetSharerResponseDto> getObjetSharers(
            @PathVariable(name = "objetId") Long objetId
    ) {
        return ResponseEntity.ok(objetSharerService.getObjetSharerList(objetId));
    }

    @GetMapping("/me")
    public ResponseEntity<List<ObjetMeResponseDto>> getMyObjets(
            @AuthenticationPrincipal Long userId
    ) {
        return ResponseEntity.ok(objetService.getMyObjetList(userId));
    }

    @PatchMapping("/{objetId}")
    public ResponseEntity<ObjetInfoResponseDto> updateObjet(
            @AuthenticationPrincipal Long userId,
            @PathVariable(name = "objetId") Long objetId,
            @RequestBody ObjetUpdateRequestDto request
    ) {
        return ResponseEntity.ok(objetService.updateObjet(request, objetId, userId));
    }

    @DeleteMapping("/{objetId}")
    public ResponseEntity<ObjetDeleteResponseDto> deleteObjet(
            @PathVariable(name = "objetId") Long objetId,
            @AuthenticationPrincipal Long userId
    ) {
        return ResponseEntity.ok(objetService.deleteObjet(objetId, userId));
    }
}
