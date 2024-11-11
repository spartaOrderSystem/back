package com.spartaordersystem.domains.user.controller.dto;

import com.spartaordersystem.domains.user.entity.User;
import com.spartaordersystem.domains.user.util.AllowedValues;
import com.spartaordersystem.global.security.user.UserRoleEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import static com.spartaordersystem.global.common.GlobalConst.ROLE_USER;

public class AuthRequestDto {

    @Getter
    @ToString
    public static class SignUp {

        @NotBlank
        @Size(min = 4, max = 10)
        @Pattern(regexp = "^[a-z0-9]+$")
        private final String username;

        @NotBlank
        @Size(min = 8, max = 15)
        @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,15}$")
        private final String password;

        @NotBlank
        private final String nickname;

        @NotBlank
        @AllowedValues(values = {ROLE_USER})
        private final String role;

        @Builder
        public SignUp(String username, String password, String nickname, String role) {
            this.username = username;
            this.password = password;
            this.nickname = nickname;
            this.role = role;
        }

        public User toUser() {
            return User.builder()
                    .username(this.username)
                    .password(this.password)
                    .role(UserRoleEnum.valueOf(this.role))
                    .nickname(this.nickname)
                    .build();
        }
    }

    @Getter
    @ToString
    public static class Login {
        @NotBlank
        @Size(min = 4, max = 10)
        @Pattern(regexp = "^[a-z0-9]+$")
        private final String username;

        @NotBlank
        @Size(min = 8, max = 15)
        @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,15}$")
        private final String password;

        @Builder
        public Login(String username, String password) {
            this.username = username;
            this.password = password;
        }
    }



}
