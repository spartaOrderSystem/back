package com.spartaordersystem.domains.UserAddress.service;

import com.spartaordersystem.domains.UserAddress.controller.dto.CreateUserAddressDto;
import com.spartaordersystem.domains.UserAddress.entity.UserAddress;
import com.spartaordersystem.domains.UserAddress.repository.UserAddressRepository;
import com.spartaordersystem.domains.store.entity.Store;
import com.spartaordersystem.domains.user.entity.User;
import com.spartaordersystem.domains.user.repository.UserRepository;
import com.spartaordersystem.global.common.GlobalConst;
import com.spartaordersystem.global.exception.CustomException;
import com.spartaordersystem.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserAddressService {

    private final UserAddressRepository userAddressRepository;
    private final UserRepository userRepository;

    @Transactional
    public CreateUserAddressDto.ResponseDto userAddressService(User user, CreateUserAddressDto.RequestDto requestDto) {
        checkUserIsNotStoreOwner(user);

        if (user.getUserAddress() != null) {
            throw new CustomException(ErrorCode.ALREADY_EXIST_ADDRESS);
        }

        UserAddress userAddress = UserAddress.builder()
                .address(requestDto.getAddress())
                .detailAddress(requestDto.getDetailAddress())
                .storeRequest(requestDto.getStoreRequest())
                .riderRequest(requestDto.getRiderRequest())
                .user(user)
                .build();

        user.setUserAddress(userAddress);
        userAddressRepository.save(userAddress);

        return CreateUserAddressDto.ResponseDto.builder()
                .userAddressId(userAddress.getId())
                .address(userAddress.getAddress())
                .detailAddress(userAddress.getDetailAddress())
                .storeRequest(userAddress.getStoreRequest())
                .riderRequest(userAddress.getRiderRequest())
                .build();
    }


    // 가게 주인만 아니면 됨
    private void checkUserIsNotStoreOwner(User user) {
        if (user.getRole().getAuthority().equals(GlobalConst.ROLE_OWNER)) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }
    }
}
