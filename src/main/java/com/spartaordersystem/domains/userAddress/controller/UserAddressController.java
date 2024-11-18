package com.spartaordersystem.domains.userAddress.controller;

import com.spartaordersystem.domains.userAddress.controller.dto.CreateUserAddressDto;
import com.spartaordersystem.domains.userAddress.controller.dto.GetUserAddressDto;
import com.spartaordersystem.domains.userAddress.controller.dto.UpdateUserAddressDto;
import com.spartaordersystem.domains.userAddress.service.UserAddressService;
import com.spartaordersystem.domains.user.entity.User;
import com.spartaordersystem.global.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

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

    @PatchMapping("/{addressId}")
    public ResponseEntity<BaseResponse> updateAddress(
            @AuthenticationPrincipal User user,
            @PathVariable UUID addressId,
            @RequestBody UpdateUserAddressDto.RequestDto requestDto
    ) {
        UpdateUserAddressDto.ResponseDto responseDto = userAddressService.updateAddress(user, addressId, requestDto);
        BaseResponse response = BaseResponse.toSuccessResponse("유저 정보가 수정되었습니다.", responseDto);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/users")
    public ResponseEntity<BaseResponse> getAddressInfo(
            @AuthenticationPrincipal User user
    ) {
        GetUserAddressDto.ResponseDto responseDto = userAddressService.getAddressInfo(user);
        BaseResponse response = BaseResponse.toSuccessResponse("주소 조회 성공", responseDto);
        return ResponseEntity.ok(response);
    }


}
