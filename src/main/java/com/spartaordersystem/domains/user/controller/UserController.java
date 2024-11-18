package com.spartaordersystem.domains.user.controller;

import com.spartaordersystem.domains.user.controller.dto.GetUserInfoDto;
import com.spartaordersystem.domains.user.entity.User;
import com.spartaordersystem.domains.user.service.UserService;
import com.spartaordersystem.global.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@RestController
public class UserController {
    private final UserService userService;

    @GetMapping("/{username}")
    public ResponseEntity<BaseResponse> getUserInfo(@PathVariable String username, @AuthenticationPrincipal User user) {
        log.info("AuthController > getUserInfo");
        GetUserInfoDto.ResponseDto responseDto = userService.getUserInfo(username, user);
        return ResponseEntity.ok().body(BaseResponse.toSuccessResponse("사용자 정보 조회 성공", responseDto));
    }
}
