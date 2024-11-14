package com.spartaordersystem.domains.ai.service;

import com.spartaordersystem.domains.ai.client.GeminiApiClient;
import com.spartaordersystem.domains.ai.controller.dto.GetPromptDto;
import com.spartaordersystem.domains.ai.controller.dto.PromptDto;
import com.spartaordersystem.domains.ai.entity.Prompt;
import com.spartaordersystem.domains.ai.repository.PromptRepository;
import com.spartaordersystem.domains.store.entity.Store;
import com.spartaordersystem.domains.store.repository.StoreRepository;
import com.spartaordersystem.domains.storeMenu.entity.StoreMenu;
import com.spartaordersystem.domains.storeMenu.repository.MenuRepository;
import com.spartaordersystem.domains.user.entity.User;
import com.spartaordersystem.global.common.GlobalConst;
import com.spartaordersystem.global.exception.CustomException;
import com.spartaordersystem.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class PromptService {

    private final GeminiApiClient geminiApiClient;
    private final PromptRepository promptRepository;
    private final MenuRepository menuRepository;
    private final StoreRepository storeRepository;


    // 기존에 존재하는 메뉴에 대한 설명 생성
    @Transactional
    public PromptDto.ResponseDto updateDescription(User user, UUID storeId, UUID menuId, PromptDto.RequestDto requestDto) {
        StoreMenu menu = getMenu(menuId);
        Store store = getStore(storeId);
        checkUserRole(user.getRole().getAuthority(), user, store);

        // 보낸 요청
        String promptContent = String.format("제목: %s, 가격: %d. %s. 답변은 최대한 간결하게 50자 이내로 해줘", menu.getTitle(), menu.getPrice(), requestDto.getDetails());

        // 요청보내고 답변 저장
        String description = geminiApiClient.createDescription(menu.getTitle(), menu.getPrice(), requestDto.getDetails());

        // 답변(메뉴 설명)  메뉴에 저장
        menu.setDescription(description);
        menuRepository.save(menu);

        Prompt prompt = Prompt.builder()
                .promptContent(promptContent)
                .answer(description)
                .storeMenu(menu)
                .build();

        promptRepository.save(prompt);

        return PromptDto.ResponseDto.builder()
                .menuId(menu.getId())
                .description(menu.getDescription())
                .build();
    }

    private StoreMenu getMenu(UUID menuId) {
        return menuRepository.findById(menuId)
                .orElseThrow(() -> new CustomException(ErrorCode.MENU_NOT_FOUND));
    }
    private Store getStore(UUID storeId) {
        return storeRepository.findById(storeId)
                .orElseThrow(() -> new CustomException(ErrorCode.STORE_NOT_FOUND));
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


    public Page<GetPromptDto.ResponseDto> getPromptHistory(int page, int size) {
        if (page < 0 || size <= 0) {
            throw new CustomException(ErrorCode.INVALID_PAGE_OR_SIZE);
        }

        int pageSize = (size == 10 || size == 30 || size == 50) ? size : 10;
        Sort sort = Sort.by("createdAt").descending();

        Pageable pageable = PageRequest.of(page, pageSize, sort);
        Page<Prompt> promptPage = promptRepository.findAll(pageable);

        List<GetPromptDto.ResponseDto> responseDtoList = promptPage.getContent().stream()
                .map(prompt -> GetPromptDto.ResponseDto.builder()
                        .id(prompt.getId())
                        .promptContent(prompt.getPromptContent())
                        .answer(prompt.getAnswer())
                        .createdAt(prompt.getCreatedAt())
                        .build())
                .toList();

        return new PageImpl<>(responseDtoList, pageable, promptPage.getTotalElements());
    }
}
