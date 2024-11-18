package com.spartaordersystem.domains.user.controller.dto;

import com.spartaordersystem.domains.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public class GetUserInfoDto {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ResponseDto {
        private String username;
        private String nickname;
        private String role;
        // TODO : 사용자 주소도 반환하려고 했는데, UserAddress 도메인에 질문드릴 게 있어 일단 보류

        public static ResponseDto toDto(User user) {
            return ResponseDto.builder()
                    .username(user.getUsername())
                    .nickname(user.getNickname())
                    .role(user.getRole().getAuthority())
                    .build();
        }
    }

}
