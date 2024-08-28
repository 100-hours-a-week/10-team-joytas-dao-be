package com.example.daobe.lounge.application;

import com.example.daobe.lounge.application.dto.LoungeSharerInfoResponseDto;
import com.example.daobe.lounge.domain.LoungeSharer;
import com.example.daobe.lounge.domain.repository.LoungeSharerRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class LoungeSearchService {

    private final LoungeSharerRepository loungeSharerRepository;

    public List<LoungeSharerInfoResponseDto> loungeSharerSearch(String nickname, Long id) {
        List<LoungeSharer> byUserId = loungeSharerRepository
                .findByLounge_IdAndUser_NicknameContaining(id, nickname);
        return LoungeSharerInfoResponseDto.of(byUserId);
    }
}
