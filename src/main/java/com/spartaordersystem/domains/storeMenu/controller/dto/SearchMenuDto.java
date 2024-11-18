package com.spartaordersystem.domains.storeMenu.controller.dto;

import com.spartaordersystem.domains.store.controller.dto.SearchStoreDto;
import com.spartaordersystem.domains.storeMenu.entity.StoreMenu;
import com.spartaordersystem.domains.storeMenu.enums.MenuStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class SearchMenuDto {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class ResponseDto {
        private UUID id;
        private String title;
        private String description;
        private long price;
        private MenuStatus menuStatus;

        public static List<ResponseDto> toDtos(Page<StoreMenu> menus) {
            return menus.stream().map(SearchMenuDto.ResponseDto::toDto).collect(Collectors.toList());
        }

        private static ResponseDto toDto(StoreMenu menu) {
            return ResponseDto.builder()
                    .id(menu.getId())
                    .title(menu.getTitle())
                    .price(menu.getPrice())
                    .description(menu.getDescription())
                    .menuStatus(menu.getMenuStatus())
                    .build();
        }
    }
}
