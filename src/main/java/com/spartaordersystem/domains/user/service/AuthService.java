package com.spartaordersystem.domains.user.service;

import com.spartaordersystem.domains.user.entity.User;
import com.spartaordersystem.domains.user.repository.UserRepository;
import com.spartaordersystem.global.exception.CustomException;
import com.spartaordersystem.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class AuthService {
    private final UserRepository userRepository;

    public void signUp(User user) {
        log.info("AuthService > signUp");
        userRepository.save(user);
    }

    public void usernameDuplicateCheck(String username) {
        if (userRepository.existsByUsername(username)) {
            throw new CustomException(ErrorCode.ALREADY_EXIST_USERNAME);
        }
    }
}
