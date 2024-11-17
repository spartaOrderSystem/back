package com.spartaordersystem.domains.order.service;

import com.spartaordersystem.domains.UserAddress.entity.UserAddress;
import com.spartaordersystem.domains.order.controller.dto.CreateOrderDto;
import com.spartaordersystem.domains.order.controller.dto.GetMyOrderListDto;
import com.spartaordersystem.domains.order.controller.dto.GetOrderInfoByOwnerDto;
import com.spartaordersystem.domains.order.controller.dto.GetOrderInfoDto;
import com.spartaordersystem.domains.order.entity.Order;
import com.spartaordersystem.domains.order.enums.OrderStatus;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
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
            throw new CustomException(ErrorCode.STORE_IS_CLOSED);
        }

        UserAddress userAddress = myUser.getUserAddress();

        Order order = Order.builder()
                .address(userAddress.getAddress())
                .detailAddress(userAddress.getDetailAddress())
                .storeRequest(userAddress.getStoreRequest())
                .riderRequest(userAddress.getRiderRequest())
                .orderStatus(OrderStatus.Pending)
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
        order.setTotalPrice(totalPrice);


        return CreateOrderDto.ResponseDto.builder()
                .orderId(order.getId())
                .storeId(storeId)
                .orderType(requestDto.getOrderType())
                .orderStatus(order.getOrderStatus())
                .storeRequest(order.getStoreRequest())
                .riderRequest(order.getRiderRequest())
                .orderMenuResponseList(orderMenuResponseList)
                .totalPrice(totalPrice)
                .build();
    }

    @Transactional
    public GetOrderInfoDto.ResponseDto getOrderInfo(User user, UUID orderId) {
        User myUser = checkUser(user);
        Order order = getOrder(orderId);

        List<OrderMenu> orderMenuList = orderMenuRepository.findByOrder(order);

        List<GetOrderInfoDto.OrderMenuResponse> orderMenuResponseList = orderMenuList.stream()
                .map(orderMenu -> GetOrderInfoDto.OrderMenuResponse.builder()
                        .menuId(orderMenu.getMenu().getId())
                        .menuName(orderMenu.getMenu().getTitle())
                        .quantity(orderMenu.getQuantity())
                        .price(orderMenu.getPrice())
                        .build())
                .toList();

        long totalPrice = order.getTotalPrice();

        return GetOrderInfoDto.ResponseDto.builder()
                .orderId(order.getId())
                .orderType(order.getOrderType())
                .orderStatus(order.getOrderStatus())
                .storeRequest(order.getStoreRequest())
                .riderRequest(order.getRiderRequest())
                .orderMenuResponseList(orderMenuResponseList)
                .totalPrice(totalPrice)
                .build();
    }

    @Transactional
    public GetOrderInfoByOwnerDto.ResponseDto getOrderInfoByOwner(User user, UUID storeId, UUID orderId) {
        Store store = getStore(storeId);
        checkUserRole(user.getRole().getAuthority(), user, store);
        Order order = getOrder(orderId);

        boolean orderIsMatchStore = isOrderMatchStore(order, store);

        if (!orderIsMatchStore) {
            throw new CustomException(ErrorCode.ORDER_NOT_MATCH_STORE);
        }

        List<OrderMenu> orderMenuList = orderMenuRepository.findByOrder(order);

        UUID orderStoreId = orderMenuList.get(0).getMenu().getStore().getId();

        List<GetOrderInfoByOwnerDto.OrderMenuResponse> orderMenuResponseList = orderMenuList.stream()
                .map(orderMenu -> GetOrderInfoByOwnerDto.OrderMenuResponse.builder()
                        .menuId(orderMenu.getMenu().getId())
                        .menuName(orderMenu.getMenu().getTitle())
                        .quantity(orderMenu.getQuantity())
                        .price(orderMenu.getPrice())
                        .build())
                .toList();

        long totalPrice = order.getTotalPrice();


        return GetOrderInfoByOwnerDto.ResponseDto.builder()
                .orderId(order.getId())
                .storeId(orderStoreId)
                .orderType(order.getOrderType())
                .orderStatus(order.getOrderStatus())
                .storeRequest(order.getStoreRequest())
                .riderRequest(order.getRiderRequest())
                .orderMenuResponseList(orderMenuResponseList)
                .totalPrice(totalPrice)
                .build();
    }

    public void updateStoreStatus(User user, UUID storeId, UUID orderId) {
        Store store = getStore(storeId);
        checkUserRole(user.getRole().getAuthority(), user, store);
        Order order = getOrder(orderId);

        switch (order.getOrderStatus()) {
            case Pending -> order.setOrderStatus(OrderStatus.Confirmed);
            case Confirmed -> order.setOrderStatus(OrderStatus.CHECKING_ORDER);
            case CHECKING_ORDER -> order.setOrderStatus(OrderStatus.COOKING);
            case COOKING -> order.setOrderStatus(OrderStatus.DELIVERING);
            default -> throw new CustomException(ErrorCode.CAN_NOT_CHANGE_ORDER_STATUS);
        }

        orderRepository.save(order);
    }

    public List<GetMyOrderListDto.ResponseDto> getMyOrderList(User user) {
        List<Order> orderList = orderRepository.findByUser(user);

        return orderList.stream()
                .map(order -> GetMyOrderListDto.ResponseDto.builder()
                        .orderId(order.getId())
                        .createdAt(order.getCreatedAt())
                        .totalPrice(order.getTotalPrice())
                        .build())
                .toList();
    }

    @Transactional
    public void deleteOrder(User user, UUID orderId) {
        Order order = getOrder(orderId);

        ZonedDateTime orderCreatedAt = order.getCreatedAt();
        ZonedDateTime currentTime = ZonedDateTime.now();

        if (ChronoUnit.MINUTES.between(orderCreatedAt, currentTime) > 5) {
            throw new CustomException(ErrorCode.CAN_NOT_CANCEL_ORDER);
        }

        if (order.getOrderStatus() != OrderStatus.Pending) {
            throw new CustomException(ErrorCode.CAN_NOT_CANCEL_ORDER_PROCESS);
        }

        if (!order.getUser().getId().equals(user.getId())) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }

        order.setOrderStatus(OrderStatus.CANCELLED);
        orderRepository.save(order);
    }

    private boolean isOrderMatchStore(Order order, Store store) {
        return orderMenuRepository.findByOrder(order).stream()
                .allMatch(orderMenu -> orderMenu.getMenu().getStore().equals(store));
    }


    private Order getOrder(UUID orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new CustomException(ErrorCode.ORDER_NOT_FOUND));
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
