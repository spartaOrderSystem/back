package com.spartaordersystem.domains.store.service;

import com.spartaordersystem.domains.category.entity.Category;
import com.spartaordersystem.domains.category.enums.CategoryType;
import com.spartaordersystem.domains.category.repository.CategoryRepository;
import com.spartaordersystem.domains.store.controller.dto.CreateStoreDto;
import com.spartaordersystem.domains.store.entity.Store;
import com.spartaordersystem.domains.store.repository.StoreRepository;
import com.spartaordersystem.domains.user.entity.User;
import com.spartaordersystem.domains.user.repository.UserRepository;
import com.spartaordersystem.global.security.user.UserRoleEnum;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class StoreServiceTest {

    @Mock
    private StoreRepository storeRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private StoreService storeService;

    private User user;
    private Category category;
    private CreateStoreDto.RequestDto requestDto;


    @BeforeEach
    void setUp() {
        user = new User(1L, "test11", "123123123", UserRoleEnum.OWNER, "testOwner");
        category = new Category(UUID.randomUUID(), CategoryType.CHICKEN, "testUser", null, null, null);
        requestDto = CreateStoreDto.RequestDto.builder()
                .title("Create Store Test")
                .address("우리집")
                .openTime(ZonedDateTime.now())
                .closeTime(ZonedDateTime.now().plusHours(12))
                .phoneNumber("111-111-111")
                .categoryId(category.getId())
                .build();
    }

    @AfterEach
    void clear() {
        user = null;
    }

    @Test
    @DisplayName("가게 생성 성공")
    void createStoreSuccess() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(categoryRepository.findById(requestDto.getCategoryId())).thenReturn(Optional.of(category));
        when(storeRepository.save(any(Store.class))).thenAnswer(invocation -> invocation.getArgument(0));

        CreateStoreDto.ResponseDto responseDto = storeService.createStore(user.getId(), user.getRole().getAuthority(), requestDto);

        assertNotNull(responseDto);
        assertEquals(requestDto.getTitle(), responseDto.getTitle());
        assertEquals(requestDto.getAddress(), responseDto.getAddress());
        assertEquals(requestDto.getPhoneNumber(), responseDto.getPhoneNumber());
        assertEquals(category.getName().name(), responseDto.getCategoryName());
        verify(storeRepository, times(1)).save(any(Store.class));
    }

}