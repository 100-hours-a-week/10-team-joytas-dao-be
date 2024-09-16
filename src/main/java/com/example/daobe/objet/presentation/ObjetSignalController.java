package com.example.daobe.objet.presentation;

import com.example.daobe.common.response.ApiResponse;
import com.example.daobe.objet.application.ObjetSignalService;
import com.example.daobe.objet.application.dto.ObjetSignalRequestDto;
import lombok.RequiredArgsConstructor;
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

    /**
     * 시그널링 서버에서 유저의 토큰과 lounge_id를 통해 해당 오브제에 접근 가능한 유저인지 판별합니다.
     *
     * @param userId  요청한 유저의 id
     * @param request lounge_id가 포함되어 있어야 합니다.
     * @return 인증 성공 시 'AUTHENTICATION_SUCCESS' 메시지를 포함한 응답
     * @author jikky
     */
    @PostMapping("/validate")
    public ResponseEntity<ApiResponse<Void>> validate(
            @AuthenticationPrincipal Long userId,
            @RequestBody ObjetSignalRequestDto request
    ) {
        objetSignalService.isObjetSharer(userId, request);
        return ResponseEntity.ok(new ApiResponse<>(
                "AUTHENTICATION_SUCCESS", null
        ));

    }
}
