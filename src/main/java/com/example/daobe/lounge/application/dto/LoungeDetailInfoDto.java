package com.example.daobe.lounge.application.dto;

import com.example.daobe.lounge.domain.Lounge;
import com.example.daobe.objet.domain.Objet;
import java.util.List;

public record LoungeDetailInfoDto(
        Long loungeId,
        String name,
        String type,
        Long userId,
        List<ObjetInfo> objets
) {
    public static LoungeDetailInfoDto of(Lounge lounge, List<ObjetInfo> objetInfos) {
        return new LoungeDetailInfoDto(
                lounge.getId(),
                lounge.getName(),
                lounge.getType().getTypeName(),
                lounge.getUser().getId(),
                objetInfos
        );
    }

    public record ObjetInfo(
            Long objetId,
            String type,
            String name
    ) {
        public static ObjetInfo of(Objet objet) {
            return new ObjetInfo(
                    objet.getId(),
                    objet.getType().getType(),
                    objet.getName()
            );
        }
    }
}
