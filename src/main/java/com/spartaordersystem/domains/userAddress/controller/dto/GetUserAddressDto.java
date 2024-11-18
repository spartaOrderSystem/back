package com.spartaordersystem.domains.userAddress.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

public class GetUserAddressDto {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class ResponseDto {

        private UUID userAddressId;
        private String address;
        private String detailAddress;
        private String storeRequest;
        private String riderRequest;

    }
}
