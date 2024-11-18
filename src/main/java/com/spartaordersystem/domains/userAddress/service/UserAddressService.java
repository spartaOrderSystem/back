package com.spartaordersystem.domains.userAddress.service;

import com.spartaordersystem.domains.userAddress.controller.dto.CreateUserAddressDto;
import com.spartaordersystem.domains.userAddress.controller.dto.GetUserAddressDto;
import com.spartaordersystem.domains.userAddress.controller.dto.UpdateUserAddressDto;
import com.spartaordersystem.domains.userAddress.entity.UserAddress;
import com.spartaordersystem.domains.userAddress.repository.UserAddressRepository;
import com.spartaordersystem.domains.user.entity.User;
import com.spartaordersystem.domains.user.repository.UserRepository;
import com.spartaordersystem.global.common.GlobalConst;
import com.spartaordersystem.global.exception.CustomException;
import com.spartaordersystem.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

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

//        userAddress.setUser(user);
        userAddressRepository.save(userAddress);
        user.setUserAddress(userAddress);
        userRepository.save(user);

        return CreateUserAddressDto.ResponseDto.builder()
                .userAddressId(userAddress.getId())
                .address(userAddress.getAddress())
                .detailAddress(userAddress.getDetailAddress())
                .storeRequest(userAddress.getStoreRequest())
                .riderRequest(userAddress.getRiderRequest())
                .build();
    }

    @Transactional
    public UpdateUserAddressDto.ResponseDto updateAddress(User user, UUID addressId, UpdateUserAddressDto.RequestDto requestDto) {
        checkUserIsNotStoreOwner(user);
        UserAddress userAddress = getUserAddress(addressId);

        userAddress.updateUserAddress(requestDto);

        return UpdateUserAddressDto.ResponseDto.builder()
                .userAddressId(userAddress.getId())
                .address(userAddress.getAddress())
                .detailAddress(userAddress.getDetailAddress())
                .storeRequest(userAddress.getStoreRequest())
                .riderRequest(userAddress.getRiderRequest())
                .build();
    }

    @Transactional
    public GetUserAddressDto.ResponseDto getAddressInfo(User user) {
        if (user.getUserAddress() == null) {
            throw new CustomException(ErrorCode.USER_ADDRESS_NOT_FOUND);
        }

        UserAddress userAddress = getUserAddress(user.getUserAddress().getId());

        return GetUserAddressDto.ResponseDto.builder()
                .userAddressId(userAddress.getId())
                .address(userAddress.getAddress())
                .detailAddress(userAddress.getDetailAddress())
                .storeRequest(userAddress.getStoreRequest())
                .riderRequest(userAddress.getRiderRequest())
                .build();
    }

    private UserAddress getUserAddress(UUID addressId) {
        return userAddressRepository.findById(addressId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_ADDRESS_NOT_FOUND));
    }

    // 가게 주인만 아니면 됨

    private void checkUserIsNotStoreOwner(User user) {
        if (user.getRole().getAuthority().equals(GlobalConst.ROLE_OWNER)) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }
    }
}
