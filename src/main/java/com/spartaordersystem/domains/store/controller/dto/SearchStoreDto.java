package com.spartaordersystem.domains.store.controller.dto;

import com.spartaordersystem.domains.store.entity.Store;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class SearchStoreDto {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class ResponseDto {
        private String title;
        private String address;
        private ZonedDateTime openTime;
        private ZonedDateTime closeTime;
        private String phoneNumber;
        private String categoryName;

        public static List<ResponseDto> toDtos(Page<Store> stores) {
            return stores.stream().map(ResponseDto::toDto).collect(Collectors.toList());
        }

        private static ResponseDto toDto(Store store) {
            return ResponseDto.builder()
                    .title(store.getTitle())
                    .address(store.getAddress())
                    .openTime(store.getOpenTime())
                    .closeTime(store.getCloseTime())
                    .phoneNumber(store.getPhoneNumber())
                    .categoryName(store.getCategory().getName())
                    .build();
        }
    }
}
