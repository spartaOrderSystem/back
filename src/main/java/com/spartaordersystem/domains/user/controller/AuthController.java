package com.spartaordersystem.domains.user.controller;

import com.spartaordersystem.domains.user.controller.dto.AuthRequestDto;
import com.spartaordersystem.domains.user.service.AuthService;
import com.spartaordersystem.global.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@RestController
public class AuthController {
    private final AuthService authService;

    @PostMapping("/sign-up")
    public ResponseEntity<Object> signUp(@RequestBody AuthRequestDto.SignUp requestDto) {
        log.info("AuthController > signUp");
        authService.signUp(requestDto.toUser());
        log.info("{}", ResponseEntity.ok().body(BaseResponse.toSuccessResponse("회원 가입 성공")));
        return ResponseEntity.ok().body(BaseResponse.toSuccessResponse("회원 가입 성공"));
    }

}
