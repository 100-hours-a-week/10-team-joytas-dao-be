package com.example.daobe.lounge.application.dto;

import com.example.daobe.objet.entity.Objet;
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
            Long objectId,
            String type,
            String name
    ) {
        public static ObjetInfo of(Objet objet) {
            return ObjetInfo.builder()
                    .objectId(objet.getObjetId())
                    .type(objet.getType().getType())
                    .name(objet.getName())
                    .build();
        }
    }
}
