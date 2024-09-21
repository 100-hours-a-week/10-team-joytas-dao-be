package com.example.daobe.objet.presentation;

import com.example.daobe.common.response.ApiResponse;
import com.example.daobe.objet.application.ObjetSignalService;
import com.example.daobe.objet.application.dto.ObjetCallParticipantsResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/objets")
@RequiredArgsConstructor
public class ObjetSignalController {

    private final ObjetSignalService objetSignalService;

    @GetMapping("{objetId}/call/participants")
    public ResponseEntity<ApiResponse<ObjetCallParticipantsResponseDto>> getCallParticipants(
            @PathVariable(name = "objetId") Long objetId
    ) {
        ObjetCallParticipantsResponseDto response = objetSignalService.getCallParticipantsByObjetId(objetId);
        return ResponseEntity.ok(new ApiResponse<>("OBJET_CALL_PARTICIPANTS_LOADED_SUCCESS", response));
    }
}
