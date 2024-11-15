package com.spartaordersystem.domains.order.service;

import com.spartaordersystem.domains.UserAddress.entity.UserAddress;
import com.spartaordersystem.domains.order.controller.dto.CreateOrderDto;
import com.spartaordersystem.domains.order.entity.Order;
import com.spartaordersystem.domains.order.repository.OrderRepository;
import com.spartaordersystem.domains.order_menu.entity.OrderMenu;
import com.spartaordersystem.domains.order_menu.repository.OrderMenuRepository;
import com.spartaordersystem.domains.store.entity.Store;
import com.spartaordersystem.domains.store.enums.StoreStatus;
import com.spartaordersystem.domains.store.repository.StoreRepository;
import com.spartaordersystem.domains.storeMenu.entity.StoreMenu;
import com.spartaordersystem.domains.storeMenu.enums.MenuStatus;
import com.spartaordersystem.domains.storeMenu.repository.MenuRepository;
import com.spartaordersystem.domains.user.entity.User;
import com.spartaordersystem.domains.user.repository.UserRepository;
import com.spartaordersystem.global.common.GlobalConst;
import com.spartaordersystem.global.exception.CustomException;
import com.spartaordersystem.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final StoreRepository storeRepository;
    private final MenuRepository menuRepository;
    private final OrderMenuRepository orderMenuRepository;

    @Transactional
    public CreateOrderDto.ResponseDto createOrder(User user, UUID storeId, CreateOrderDto.RequestDto requestDto) {
        User myUser = checkUser(user);
        Store store = getStore(storeId);

        if (store.getStoreStatus() == StoreStatus.CLOSE) {
            throw new CustomException(ErrorCode.STROE_IS_CLOSED);
        }

        UserAddress userAddress = myUser.getUserAddress();

        Order order = Order.builder()
                .address(userAddress.getAddress())
                .detailAddress(userAddress.getDetailAddress())
                .storeRequest(userAddress.getStoreRequest())
                .riderRequest(userAddress.getRiderRequest())
                .orderStatus(requestDto.getOrderStatus())
                .orderType(requestDto.getOrderType())
                .user(user)
                .build();

        orderRepository.save(order);

        List<CreateOrderDto.OrderMenuResponse> orderMenuResponseList =
                requestDto.getOrderMenuRequestList().stream()
                        .map(orderMenuRequest -> {
                            StoreMenu menu = menuRepository.findById(orderMenuRequest.getMenuId())
                                    .orElseThrow(() -> new CustomException(ErrorCode.MENU_NOT_FOUND));

                            if (menu.getMenuStatus() == MenuStatus.SOLD_OUT) {
                                throw new CustomException(ErrorCode.MENU_IS_SOLD_OUT);
                            }

                            orderMenuRepository.save(OrderMenu.builder()
                                    .quantity(orderMenuRequest.getQuantity())
                                    .price(menu.getPrice())
                                    .order(order)
                                    .menu(menu)
                                    .build());

                            return CreateOrderDto.OrderMenuResponse.builder()
                                    .menuId(orderMenuRequest.getMenuId())
                                    .quantity(orderMenuRequest.getQuantity())
                                    .price(menu.getPrice())
                                    .build();
                        })
                        .toList();

        long totalPrice = calculateTotalPrice(orderMenuResponseList);


        return CreateOrderDto.ResponseDto.builder()
                .orderId(order.getId())
                .storeId(storeId)
                .orderType(requestDto.getOrderType())
                .storeRequest(order.getStoreRequest())
                .riderRequest(order.getRiderRequest())
                .orderMenuResponseList(orderMenuResponseList)
                .totalPrice(totalPrice)
                .build();
    }

    private long calculateTotalPrice(List<CreateOrderDto.OrderMenuResponse> orderMenuResponseList) {
        return orderMenuResponseList.stream()
                .mapToLong(response -> response.getPrice() * response.getQuantity())
                .sum();
    }

    private User checkUser(User user) {
        return userRepository.findById(user.getId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
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
        } else if (!(userRole.equals(GlobalConst.ROLE_MANAGER) || userRole.equals(GlobalConst.ROLE_ADMIN))) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }
    }

    // 손님만 아니면 될 경우
    private void checkUserRole(String userRole) {
        if (!(userRole.equals(GlobalConst.ROLE_OWNER) || userRole.equals(GlobalConst.ROLE_MANAGER) || userRole.equals(GlobalConst.ROLE_ADMIN))) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }
    }

    private Store getStore(UUID storeId) {
        return storeRepository.findById(storeId)
                .orElseThrow(() -> new CustomException(ErrorCode.STORE_NOT_FOUND));
    }
}
