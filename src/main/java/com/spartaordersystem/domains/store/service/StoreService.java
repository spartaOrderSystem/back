package com.spartaordersystem.domains.store.service;

import com.spartaordersystem.domains.category.repository.CategoryRepository;
import com.spartaordersystem.domains.store.controller.dto.CreateStoreDto;
import com.spartaordersystem.domains.store.controller.dto.GetStoreDto;
import com.spartaordersystem.domains.store.controller.dto.UpdateStoreDto;
import com.spartaordersystem.domains.store.entity.Store;
import com.spartaordersystem.domains.store.repository.StoreRepository;
import com.spartaordersystem.domains.user.entity.User;
import com.spartaordersystem.domains.user.repository.UserRepository;
import com.spartaordersystem.global.exception.CustomException;
import com.spartaordersystem.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StoreService {

    private final StoreRepository storeRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    public CreateStoreDto.ResponseDto createStore(User user, CreateStoreDto.RequestDto requestDto) {
        checkUser(user);
        checkUserRole(user.getRole().getAuthority());

        Store store = Store.builder()
                .title(requestDto.getTitle())
                .address(requestDto.getAddress())
                .phoneNumber(requestDto.getPhoneNumber())
                .openTime(requestDto.getOpenTime())
                .closeTime(requestDto.getCloseTime())
                .build();

        storeRepository.save(store);

        return CreateStoreDto.ResponseDto.builder()
                .id(store.getId())
                .title(store.getTitle())
                .address(store.getAddress())
                .openTime(store.getOpenTime())
                .closeTime(store.getCloseTime())
                .phoneNumber(store.getPhoneNumber())
                .build();
    }

    public UpdateStoreDto.ResponseDto updateStore(UUID storeId, User user,UpdateStoreDto.RequestDto requestDto) {
        checkUser(user);
        Store store = getStore(storeId);
        checkUserRole(user.getRole().getAuthority(), user, store);

        store.updateStore(requestDto);

        storeRepository.save(store);

        return UpdateStoreDto.ResponseDto.builder()
                .id(storeId)
                .title(store.getTitle())
                .address(store.getAddress())
                .openTime(store.getOpenTime())
                .closeTime(store.getCloseTime())
                .phoneNumber(store.getPhoneNumber())
                .build();
    }

    public void deleteStore(UUID storeId, User user) {
        checkUser(user);
        Store store = getStore(storeId);
        checkUserRole(user.getRole().getAuthority(), user, store);

        store.setDeleted(user.getUsername());
        storeRepository.save(store);
    }

    public GetStoreDto.ResponseDto getStoreInfo(UUID storeId) {
        Store store = getStore(storeId);

        return GetStoreDto.ResponseDto.builder()
                .title(store.getTitle())
                .address(store.getAddress())
                .openTime(store.getOpenTime())
                .closeTime(store.getCloseTime())
                .phoneNumber(store.getPhoneNumber())
                .categoryName(store.getCategory().getName())
                .build();
    }


    private void checkUser(User user) {
        userRepository.findById(user.getId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }

    private void checkUserIsStoreOwner(User user, Store store) {
        if (!store.getUser().getId().equals(user.getId())) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }
    }

    private void checkUserRole(String userRole, User user, Store store) {
        if (userRole.equals("ROLE_OWNER")) {
            checkUserIsStoreOwner(user, store);
        }
        else if (!(userRole.equals("ROLE_MANAGER") || userRole.equals("ROLE_ADMIN"))) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }
    }

    private void checkUserRole(String userRole) {
        if (!(userRole.equals("ROLE_OWNER") || userRole.equals("ROLE_MANEGER") || userRole.equals("ROLE_ADMIN"))) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }
    }

    private Store getStore(UUID storeId) {
        return storeRepository.findById(storeId)
                .orElseThrow(() -> new CustomException(ErrorCode.STORE_NOT_FOUND));
    }
}
