package com.spartaordersystem.domains.storeMenu.service;

import com.spartaordersystem.domains.store.entity.Store;
import com.spartaordersystem.domains.store.repository.StoreRepository;
import com.spartaordersystem.domains.storeMenu.controller.dto.CreateMenuDto;
import com.spartaordersystem.domains.storeMenu.controller.dto.GetMenuDto;
import com.spartaordersystem.domains.storeMenu.controller.dto.GetMenuListDto;
import com.spartaordersystem.domains.storeMenu.controller.dto.UpdateMenuDto;
import com.spartaordersystem.domains.storeMenu.entity.StoreMenu;
import com.spartaordersystem.domains.storeMenu.enums.MenuStatus;
import com.spartaordersystem.domains.storeMenu.repository.MenuRepository;
import com.spartaordersystem.domains.user.entity.User;
import com.spartaordersystem.global.common.GlobalConst;
import com.spartaordersystem.global.exception.CustomException;
import com.spartaordersystem.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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
                .id(menu.getId())
                .title(menu.getTitle())
                .description(menu.getDescription())
                .price(menu.getPrice())
                .build();
    }

    @Transactional
    public UpdateMenuDto.ResponseDto updateMenu(User user, UUID storeId, UUID menuId, UpdateMenuDto.RequestDto requestDto) {
        Store store = getStore(storeId);
        checkUserRole(user.getRole().getAuthority(), user, store);
        StoreMenu menu = getMenu(menuId);

        menu.updateMenu(requestDto);

        menuRepository.save(menu);

        return UpdateMenuDto.ResponseDto.builder()
                .id(menu.getId())
                .title(menu.getTitle())
                .description(menu.getDescription())
                .price(menu.getPrice())
                .build();
    }

    @Transactional
    public void deleteMenu(User user, UUID storeId, UUID menuId) {
        Store store = getStore(storeId);
        checkUserRole(user.getRole().getAuthority(), user, store);
        StoreMenu menu = getMenu(menuId);

        menu.setDeleted(user.getUsername());
        menuRepository.save(menu);
    }

    @Transactional
    public void updateMenuStatus(User user, UUID storeId, UUID menuId) {
        Store store = getStore(storeId);
        checkUserRole(user.getRole().getAuthority(), user, store);
        StoreMenu menu = getMenu(menuId);

        if (menu.getMenuStatus() == MenuStatus.ON_SALE) {
            menu.setMenuStatus(MenuStatus.SOLD_OUT);
        } else {
            menu.setMenuStatus(MenuStatus.ON_SALE);
        }

        menuRepository.save(menu);
    }

    @Transactional(readOnly = true)
    public GetMenuDto.ResponseDto getMenuInfo(UUID storeId, UUID menuId) {
        Store store = getStore(storeId);
        StoreMenu menu = getMenu(menuId);

        if (menu.getMenuStatus() == MenuStatus.SOLD_OUT || menu.isDeleted()) {
            throw new CustomException(ErrorCode.MENU_NOT_FOUND);
        }

        return GetMenuDto.ResponseDto.builder()
                .id(menu.getId())
                .title(menu.getTitle())
                .description(menu.getDescription())
                .price(menu.getPrice())
                .build();
    }

    public List<GetMenuListDto.ResponseDto> getMenuList(UUID storeId) {
        Store store = getStore(storeId);

        List<StoreMenu> menuList = menuRepository.findByStoreAndMenuStatusAndIsDeletedFalse(store, MenuStatus.ON_SALE);

        return menuList.stream()
                .map(menu -> GetMenuListDto.ResponseDto.builder()
                        .id(menu.getId())
                        .title(menu.getTitle())
                        .description(menu.getDescription())
                        .price(menu.getPrice())
                        .build())
                .toList();
    }

    private void checkUserIsStoreOwner(User user, Store store) {
        if (!store.getUser().getId().equals(user.getId())) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }
    }

    // 손님이 아니며, 가게주인인지 검증이 필요한 경우

    private void checkUserRole(String userRole, User user, Store store) {
        if (userRole.equals(GlobalConst.ROLE_OWNER)) {
            checkUserIsStoreOwner(user, store);
        }
        else if (!(userRole.equals(GlobalConst.ROLE_MANAGER) || userRole.equals(GlobalConst.ROLE_ADMIN))) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }
    }

    private Store getStore(UUID storeId) {
        return storeRepository.findById(storeId)
                .orElseThrow(() -> new CustomException(ErrorCode.STORE_NOT_FOUND));
    }

    private StoreMenu getMenu(UUID menuId) {
        return menuRepository.findById(menuId)
                .orElseThrow(() -> new CustomException(ErrorCode.MENU_NOT_FOUND));
    }
}
