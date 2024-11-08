package com.spartaordersystem.domains.category.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CategoryType {
    CHICKEN("치킨"),
    PIZZA("피자"),
    CHINESE("중식"),
    FAST_FOOD("패스트푸드"),
    KOREAN("한식"),
    SNACK_FOOD("분식"),
    DESSERT("디저트")
    ;

    private final String description;


}
