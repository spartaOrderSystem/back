package com.spartaordersystem.domains.user.service;

import com.spartaordersystem.domains.user.controller.dto.GetUserInfoDto;
import com.spartaordersystem.domains.user.entity.User;
import com.spartaordersystem.global.exception.CustomException;
import com.spartaordersystem.global.exception.ErrorCode;
import com.spartaordersystem.global.security.user.UserRoleEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly=true)
@Service
public class UserService {

    public GetUserInfoDto.ResponseDto getUserInfo(String username, User user) {
        String authority = user.getRole().getAuthority();
        if(user.getUsername().equals(username) || authority.equals(UserRoleEnum.Authority.ADMIN) || authority.equals(UserRoleEnum.Authority.MANAGER) ) {
            return GetUserInfoDto.ResponseDto.toDto(user);
        }
        else throw new CustomException(ErrorCode.ACCESS_DENIED);
    }
}
