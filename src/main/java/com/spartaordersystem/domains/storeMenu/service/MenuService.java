package com.spartaordersystem.domains.storeMenu.service;

import com.spartaordersystem.domains.storeMenu.controller.dto.CreateMenuDto;
import com.spartaordersystem.domains.storeMenu.entity.StoreMenu;
import com.spartaordersystem.domains.storeMenu.repository.MenuRepository;
import com.spartaordersystem.domains.store.entity.Store;
import com.spartaordersystem.domains.store.repository.StoreRepository;
import com.spartaordersystem.domains.user.entity.User;
import com.spartaordersystem.global.exception.CustomException;
import com.spartaordersystem.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MenuService {

    private final MenuRepository menuRepository;
    private final StoreRepository storeRepository;

    @Transactional
    public CreateMenuDto.ResponseDto createMenu(User user, UUID storeId, CreateMenuDto.RequestDto requestDto) {
        Store store = getStore(storeId);
        checkUserRole(user.getRole().getAuthority(), user, store);

        StoreMenu menu = StoreMenu.builder()
                .title(requestDto.getTitle())
                .description(requestDto.getDescription())
                .price(requestDto.getPrice())
                .store(store)
                .build();

        menuRepository.save(menu);

        return CreateMenuDto.ResponseDto.builder()
                .title(menu.getTitle())
                .description(menu.getDescription())
                .price(menu.getPrice())
                .build();
    }

    private void checkUserIsStoreOwner(User user, Store store) {
        if (!store.getUser().getId().equals(user.getId())) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }
    }

    // 손님이 아니며, 가게주인인지 검증이 필요한 경우
    private void checkUserRole(String userRole, User user, Store store) {
        if (userRole.equals("ROLE_OWNER")) {
            checkUserIsStoreOwner(user, store);
        }
        else if (!(userRole.equals("ROLE_MANAGER") || userRole.equals("ROLE_ADMIN"))) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }
    }

    private Store getStore(UUID storeId) {
        return storeRepository.findById(storeId)
                .orElseThrow(() -> new CustomException(ErrorCode.STORE_NOT_FOUND));
    }
}
