package com.spartaordersystem.global.security.user;

import lombok.Getter;

import java.util.Arrays;

public enum UserRoleEnum {
    USER(Authority.USER),  // 사용자 권한
    OWNER(Authority.OWNER),  // 사용자 권한
    MANAGER(Authority.MANAGER),  // 사용자 권한
    ADMIN(Authority.ADMIN);  // 관리자 권한

    private final String authority;

    UserRoleEnum(String authority) {
        this.authority = authority;
    }

    public static UserRoleEnum getInstance(String authority) {
        return Arrays.stream(values())
                .filter(role -> role.getAuthority().equals(authority))
                .findFirst()
                .orElseThrow();
    }

    public String getAuthority() {
        return this.authority;
    }

    public static class Authority {
        public static final String USER = "ROLE_USER";
        public static final String OWNER = "ROLE_OWNER";
        public static final String MANAGER = "ROLE_MANAGER";
        public static final String ADMIN = "ROLE_ADMIN";
    }
}
