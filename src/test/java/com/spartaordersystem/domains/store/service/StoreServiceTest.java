//package com.spartaordersystem.domains.store.service;
//
//import com.spartaordersystem.domains.category.entity.Category;
//import com.spartaordersystem.domains.category.enums.CategoryType;
//import com.spartaordersystem.domains.category.repository.CategoryRepository;
//import com.spartaordersystem.domains.store.controller.dto.CreateStoreDto;
//import com.spartaordersystem.domains.store.controller.dto.GetStoreDto;
//import com.spartaordersystem.domains.store.controller.dto.UpdateStoreDto;
//import com.spartaordersystem.domains.store.entity.Store;
//import com.spartaordersystem.domains.store.repository.StoreRepository;
//import com.spartaordersystem.domains.user.entity.User;
//import com.spartaordersystem.domains.user.repository.UserRepository;
//import com.spartaordersystem.global.security.user.UserRoleEnum;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.time.ZonedDateTime;
//import java.util.Optional;
//import java.util.UUID;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//
//
//@ExtendWith(MockitoExtension.class)
//class StoreServiceTest {
//
//    @Mock
//    private StoreRepository storeRepository;
//
//    @Mock
//    private CategoryRepository categoryRepository;
//
//    @Mock
//    private UserRepository userRepository;
//
//    @InjectMocks
//    private StoreService storeService;
//
//    private User user;
//    private Category category;
//    private CreateStoreDto.RequestDto requestDto;
//
//
//    @BeforeEach
//    void setUp() {
//        user = new User(1L, "test11", "123123123", UserRoleEnum.OWNER, "testOwner");
//        category = Category.builder()
//                .name("치킨")
//                .build();
//        requestDto = CreateStoreDto.RequestDto.builder()
//                .title("Create Store Test")
//                .address("우리집")
//                .openTime(ZonedDateTime.now())
//                .closeTime(ZonedDateTime.now().plusHours(12))
//                .phoneNumber("111-111-111")
//                .categoryName("치킨")
//                .build();
//    }
//
//    @AfterEach
//    void clear() {
//        user = null;
////        category = null;
//    }
//
//    @Test
//    @DisplayName("가게 생성 성공")
//    void createStoreSuccess() {
////        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
////        when(categoryRepository.findByName(requestDto.getCategoryName())).thenReturn(Optional.of(category));
//        when(storeRepository.save(any(Store.class))).thenAnswer(invocation -> invocation.getArgument(0));
//
//        CreateStoreDto.ResponseDto responseDto = storeService.createStore(user, requestDto);
//        responseDto.setCategoryName("치킨");
//
//        assertNotNull(responseDto);
//        assertEquals(requestDto.getTitle(), responseDto.getTitle());
//        assertEquals(requestDto.getAddress(), responseDto.getAddress());
//        assertEquals(requestDto.getPhoneNumber(), responseDto.getPhoneNumber());
////        assertEquals(category.getName(), responseDto.getCategoryName());
//        verify(storeRepository, times(1)).save(any(Store.class));
//    }
//
//    @Test
//    @DisplayName("가게 정보 수정 성공")
//    void updateStoreSuccess() {
//        Store store = Store.builder()
//                .user(user)
//                .title("가게 이름")
//                .address("가게 주소")
//                .openTime(ZonedDateTime.now())
//                .closeTime(ZonedDateTime.now().plusHours(12))
//                .category(category)
//                .build();
//
//        category.setName("피자");
//
//        UpdateStoreDto.RequestDto updateRequestDto = UpdateStoreDto.RequestDto.builder()
//                .title("수정된 가게 이름")
//                .address("수정된 가게 주소")
//                .phoneNumber("1121-111-1211")
//                .openTime(store.getOpenTime())
//                .closeTime(store.getCloseTime())
//                .categoryName("피자")
//                .build();
//
//        when(storeRepository.findById(store.getId())).thenReturn(Optional.of(store));
//        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
//        when(categoryRepository.findById(any(UUID.class))).thenReturn(Optional.of(category));
//        when(storeRepository.save(any(Store.class))).thenAnswer(invocation -> invocation.getArgument(0));
//
//        UpdateStoreDto.ResponseDto responseDto = storeService.updateStore(store.getId(), user, updateRequestDto);
//
//        assertNotNull(responseDto);
//        assertEquals(updateRequestDto.getTitle(), responseDto.getTitle());
//        assertEquals(updateRequestDto.getAddress(), responseDto.getAddress());
//        assertEquals(updateRequestDto.getPhoneNumber(), responseDto.getPhoneNumber());
//        assertEquals(category.getName(), responseDto.getCategoryName());
//        verify(storeRepository, times(1)).save(any(Store.class));
//    }
//
//    @Test
//    @DisplayName("가게 정보 단일 조회")
//    void getStoreInfoSuccess() {
//        UUID storeId = UUID.randomUUID();
//        Store store = mock(Store.class);
//        when(store.getTitle()).thenReturn("가게 이름");
//        when(store.getAddress()).thenReturn("서울시 우리집");
//        when(store.getOpenTime()).thenReturn(ZonedDateTime.now());
//        when(store.getCloseTime()).thenReturn(ZonedDateTime.now().plusHours(3));
//        when(store.getPhoneNumber()).thenReturn("111-333-555");
//        when(store.getCategory()).thenReturn(category);
//
//        when(storeRepository.findById(storeId)).thenReturn(Optional.of(store));
//
//        GetStoreDto.ResponseDto responseDto = storeService.getStoreInfo(storeId);
//
//        assertNotNull(responseDto);
//        assertEquals(store.getTitle(), responseDto.getTitle());
//        assertEquals(store.getAddress(), responseDto.getAddress());
//        assertEquals(store.getOpenTime(), responseDto.getOpenTime());
//        assertEquals(store.getCloseTime(), responseDto.getCloseTime());
//        assertEquals(store.getPhoneNumber(), responseDto.getPhoneNumber());
//        assertEquals(category.getName(), responseDto.getCategoryName());
//    }
//
//}