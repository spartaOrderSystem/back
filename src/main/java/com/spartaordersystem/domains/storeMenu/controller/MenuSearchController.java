package com.spartaordersystem.domains.storeMenu.controller;

import com.spartaordersystem.domains.storeMenu.controller.dto.SearchMenuDto;
import com.spartaordersystem.domains.storeMenu.service.MenuService;
import com.spartaordersystem.global.exception.CustomException;
import com.spartaordersystem.global.exception.ErrorCode;
import com.spartaordersystem.global.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/menus")
public class MenuSearchController {

    private final MenuService menuService;

    @GetMapping
    public ResponseEntity<BaseResponse> searchMenu(
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long minPrice,
            @RequestParam(required = false) Long maxPrice,
            @RequestParam(required = false) String menuStatus
    ) {
        log.info("StoreController > searchStore keyword : {}, minPrice : {}, maxPrice : {}, menuStatus : {}", keyword, minPrice, maxPrice, menuStatus);

        checkPagenationInfos(page, size);

        List<SearchMenuDto.ResponseDto> responseDtos = menuService.searchMenu(page, size, keyword, minPrice, maxPrice, menuStatus);
        BaseResponse response = BaseResponse.toSuccessResponse("가게 메뉴 검색", responseDtos);
        return ResponseEntity.ok(response);
    }

    private void checkPagenationInfos(Integer page, Integer size) {
        List<Integer> sizeList = new ArrayList<>(Arrays.asList(10, 30, 50));
        if (page < -1 || size < 0 || !sizeList.contains(size)) {
            throw new CustomException(ErrorCode.INVALID_PAGE_OR_SIZE);
        }
    }
}
