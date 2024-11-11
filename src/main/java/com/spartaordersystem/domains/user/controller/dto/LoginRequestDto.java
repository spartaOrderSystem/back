package com.spartaordersystem.domains.user.controller.dto;

import lombok.Getter;

// TODO : security 용 임시 dto 임. 실제 사용 DTO 로 변경하기
@Getter
public class LoginRequestDto {

    private String username;
    private String password;
}
