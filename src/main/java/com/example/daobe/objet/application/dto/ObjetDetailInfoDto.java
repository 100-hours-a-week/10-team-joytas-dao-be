package com.example.daobe.objet.application.dto;

import com.example.daobe.objet.domain.ObjetType;
import java.util.List;

public record ObjetDetailInfoDto(
        Long objetId,
        ObjetMetadataDto objetMetadataDto,
        Boolean isActive,
        Long callingUserNum,
        List<ViewerInfo> viewers,
        List<SharerInfo> sharers
) {
    public record SharerInfo(
            Long userId,
            String nickname
    ) {
        public static SharerInfo of(Long userId, String nickname) {
            return new SharerInfo(
                    userId,
                    nickname
            );
        }
    }

    public record ViewerInfo(
            Long userId,
            String profileImage
    ) {
        public static ViewerInfo of(Long userId, String profileImage) {
            return new ViewerInfo(userId, profileImage);
        }
    }

    public record ObjetMetadataDto(
            Long userId,
            Long loungeId,
            String name,
            String nickname,
            String objetImage,
            String description,
            ObjetType type
    ) {
        public static ObjetMetadataDto of(
                Long userId,
                Long loungeId,
                String name,
                String nickname,
                String objetImage,
                String description,
                ObjetType type
        ) {
            return new ObjetMetadataDto(
                    userId,
                    loungeId,
                    name,
                    nickname,
                    objetImage,
                    description,
                    type
            );
        }
    }
}
