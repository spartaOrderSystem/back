package com.spartaordersystem.domains.storeMenu.repository;

import com.spartaordersystem.domains.store.entity.Store;
import com.spartaordersystem.domains.storeMenu.entity.StoreMenu;
import com.spartaordersystem.domains.storeMenu.enums.MenuStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MenuRepository extends JpaRepository<StoreMenu, UUID> {
    List<StoreMenu> findByStoreAndMenuStatusAndIsDeletedFalse(Store store, MenuStatus menuStatus);
}
