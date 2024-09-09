package com.example.daobe.objet.application.dto;

import com.example.daobe.objet.domain.ObjetType;
import java.util.List;
import lombok.Builder;

@Builder
public record ObjetDetailInfoDto(
        Long objetId,
        Long userId,
        Long loungeId,
        String name,
        String nickname,
        String objetImage,
        String description,
        ObjetType type,
        Boolean isActive,
        Long callingUserNum,
        List<ViewerInfo> viewers,
        List<SharerInfo> sharers
) {
    @Builder
    public record SharerInfo(
            Long userId,
            String nickname
    ) {
        public static SharerInfo of(Long userId, String nickname) {
            return SharerInfo.builder()
                    .userId(userId)
                    .nickname(nickname)
                    .build();
        }
    }

    @Builder
    public record ViewerInfo(
            Long userId,
            String profileImage
    ) {
        public static ViewerInfo of(Long userId, String profileImage) {
            return ViewerInfo.builder()
                    .userId(userId)
                    .profileImage(profileImage)
                    .build();
        }
    }
}
