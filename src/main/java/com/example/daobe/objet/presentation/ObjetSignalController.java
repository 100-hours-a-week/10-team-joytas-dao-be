package com.example.daobe.objet.presentation;

import com.example.daobe.common.response.ApiResponse;
import com.example.daobe.objet.application.ObjetSignalService;
import com.example.daobe.objet.application.dto.ObjetSignalRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/objets")
@RequiredArgsConstructor
public class ObjetSignalController {

    private final ObjetSignalService objetSignalService;

    @PostMapping("/signaling")
    public ResponseEntity<ApiResponse> signaling(
            @AuthenticationPrincipal Long userId,
            @RequestBody ObjetSignalRequestDto request
    ) {
        Boolean isValidSharer = objetSignalService.isObjetSharer(userId, request);

        if (isValidSharer) {
            return ResponseEntity.ok(new ApiResponse<>(
                    "AUTHENTICATION_SUCCESS", userId
            ));
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ApiResponse<>(
                "ACCESS_FORBIDDEN", userId
        ));
    }
}
