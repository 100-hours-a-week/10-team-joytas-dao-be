package com.example.daobe.lounge.application.dto;

import com.example.daobe.objet.domain.Objet;
import java.util.List;
import lombok.Builder;

@Builder
public record LoungeDetailInfoDto(
        Long loungeId,
        String name,
        String type,
        Long userId,
        List<ObjetInfo> objets
) {
    @Builder
    public record ObjetInfo(
            Long objetId,
            String type,
            String name
    ) {
        public static ObjetInfo of(Objet objet) {
            return ObjetInfo.builder()
                    .objetId(objet.getObjetId())
                    .type(objet.getType().getType())
                    .name(objet.getName())
                    .build();
        }
    }
}
