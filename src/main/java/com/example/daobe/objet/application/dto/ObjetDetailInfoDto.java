package com.example.daobe.objet.application.dto;

import java.util.List;
import lombok.Builder;

@Builder
public record ObjetDetailInfoDto(
        Long objetId,
        Long userId,
        String name,
        String nickname,
        String objetImage,
        String description,
        Boolean isActive,
        Long callingUserNum,
        List<ViewerInfo> viewers,
        List<ChattingInfo> chattings,
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
        // 생성 메서드
        public static ViewerInfo of(Long userId, String profileImage) {
            return ViewerInfo.builder()
                    .userId(userId)
                    .profileImage(profileImage)
                    .build();
        }
    }

    @Builder
    public record ChattingInfo(
            Long userId,
            String profileUrl,
            String name,
            String content
    ) {
        public static ChattingInfo of(Long userId, String profileUrl, String name, String content) {
            return ChattingInfo.builder()
                    .userId(userId)
                    .profileUrl(profileUrl)
                    .name(name)
                    .content(content)
                    .build();
        }
    }
}
