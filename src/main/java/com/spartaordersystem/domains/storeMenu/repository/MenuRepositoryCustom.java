package com.spartaordersystem.domains.storeMenu.repository;

import com.spartaordersystem.domains.storeMenu.entity.StoreMenu;
import com.spartaordersystem.domains.storeMenu.enums.MenuStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface MenuRepositoryCustom {
    Page<StoreMenu> getMenusBySearchOptions(Pageable pageable, String keyword, Long maxPrice, Long minPrice, MenuStatus menuStatus);
}
