package com.example.daobe.lounge.presentation;

import com.example.daobe.common.response.ApiResponse;
import com.example.daobe.lounge.application.LoungeSearchService;
import com.example.daobe.lounge.application.dto.LoungeSharerInfoResponseDto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/lounges")
@RequiredArgsConstructor
public class LoungeSearchController {

    private final LoungeSearchService loungeSearchService;

    @GetMapping("/{loungeId}/search")
    public ResponseEntity<ApiResponse<List<LoungeSharerInfoResponseDto>>> testLounge(
            @PathVariable Long loungeId,
            @RequestParam(value = "nickname", defaultValue = "") String nickname
    ) {
        return ResponseEntity.ok(new ApiResponse<>(
                "LOUNGE_SHARER_INFO_LOADED_SUCCESS",
                loungeSearchService.loungeSharerSearch(nickname, loungeId)
        ));
    }
}
