package com.spartaordersystem.domains.user.controller;

import com.spartaordersystem.domains.user.controller.dto.AuthRequestDto;
import com.spartaordersystem.domains.user.service.AuthService;
import com.spartaordersystem.global.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@RestController
public class AuthController {
    private final AuthService authService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/sign-up")
    public ResponseEntity<BaseResponse> signUp(@RequestBody AuthRequestDto.SignUp requestDto) {
        log.info("AuthController > signUp");
        authService.signUp(requestDto.toUser(passwordEncoder.encode(requestDto.getPassword())));
        return ResponseEntity.ok().body(BaseResponse.toSuccessResponse("회원 가입 성공"));
    }

    @GetMapping("/duplicate-check")
    public ResponseEntity<BaseResponse> usernameDuplicateCheck(@RequestParam String username) {
        log.info("AuthController > idDuplicateCheck");
        authService.usernameDuplicateCheck(username);
        return ResponseEntity.ok().body(BaseResponse.toSuccessResponse("아이디 중복체크 성공"));
    }

}
