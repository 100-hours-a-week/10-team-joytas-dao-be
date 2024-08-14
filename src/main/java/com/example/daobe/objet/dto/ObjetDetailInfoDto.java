package com.example.daobe.objet.dto;

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
        Long CallingUserNum,
        List<ViewerInfo> viewers,
        List<ChattingInfo> chattings
) {

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