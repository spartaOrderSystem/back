package com.spartaordersystem.domains.UserAddress.controller;

import com.spartaordersystem.domains.UserAddress.controller.dto.CreateUserAddressDto;
import com.spartaordersystem.domains.UserAddress.service.UserAddressService;
import com.spartaordersystem.domains.user.entity.User;
import com.spartaordersystem.global.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/address")
public class UserAddressController {

    private final UserAddressService userAddressService;

    @PostMapping
    public ResponseEntity<BaseResponse> createAddress(
            @AuthenticationPrincipal User user,
            @RequestBody CreateUserAddressDto.RequestDto requestDto
    ) {
        CreateUserAddressDto.ResponseDto responseDto = userAddressService.userAddressService(user, requestDto);
        BaseResponse response = BaseResponse.toSuccessResponse("주소가 생성되었습니다.", responseDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


}
