package com.example.outsourcing_11.domain.order.service;

import com.example.outsourcing_11.common.exception.CustomException;
import com.example.outsourcing_11.common.exception.ErrorCode;
import com.example.outsourcing_11.domain.menu.entity.Menu;
import com.example.outsourcing_11.domain.menu.repository.MenuRepository;
import com.example.outsourcing_11.domain.order.dto.*;
import com.example.outsourcing_11.domain.order.entity.Order;
import com.example.outsourcing_11.domain.order.entity.OrderItem;
import com.example.outsourcing_11.domain.order.entity.OrderStatus;
import com.example.outsourcing_11.domain.order.repository.OrderRepository;
import com.example.outsourcing_11.domain.store.entity.Store;
import com.example.outsourcing_11.domain.store.repository.StoreRepository;
import com.example.outsourcing_11.domain.user.entity.User;
import com.example.outsourcing_11.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final MenuRepository menuRepository;
    private final StoreRepository storeRepository;

    /**
     * 새로운 주문을 생성. 주문자는 User, 주문 항목들을 포함한다.
     * 주문 총액도 항목 기준으로 계산된다.
     */
    @Override
    public OrderResponseDto createOrder(OrderRequestDto requestDto) {
        User user = userRepository.findById(requestDto.getUserId())
                .orElseThrow(() -> new CustomException(
                        ErrorCode.USER_NOT_FOUND.getMessage(),
                        ErrorCode.USER_NOT_FOUND.getHttpStatus()
                ));


        List<OrderItem> items = new ArrayList<>();
        List<OrderItemResponseDto> itemResponses = new ArrayList<>();
        int totalPrice = 0;
        Store store = null; // ✅ 대표 store 지정

        for (OrderItemRequestDto dto : requestDto.getItems()) {
            Menu menu = menuRepository.findById(dto.getMenuId())
                    .orElseThrow(() -> new CustomException(
                            ErrorCode.MENU_NOT_FOUND.getMessage(),
                            ErrorCode.MENU_NOT_FOUND.getHttpStatus()
                    ));

            store = storeRepository.findById(dto.getStoreId())
                    .orElseThrow(() -> new CustomException(
                            ErrorCode.STORE_NOT_FOUND.getMessage(),
                            ErrorCode.STORE_NOT_FOUND.getHttpStatus()
                    ));

            OrderItem item = new OrderItem();
            item.setMenu(menu);
            item.setStore(store);
            item.setQuantity(dto.getQuantity());
            item.setItemPrice(dto.getItemPrice());
            items.add(item);

            totalPrice += dto.getItemPrice() * dto.getQuantity();

            itemResponses.add(OrderItemResponseDto.builder()
                    .id(null)
                    .menuId(dto.getMenuId())
                    .storeId(dto.getStoreId())
                    .quantity(dto.getQuantity())
                    .itemPrice(dto.getItemPrice())
                    .build());
        }

        // ✅ for문 바깥에서 Order 객체 생성 (한 번만!)
        Order order = new Order(
                user,
                store, // ✅ 대표 store
                LocalDateTime.now(),
                OrderStatus.PENDING,
                totalPrice,
                items
        );

        orderRepository.save(order);

        return new OrderResponseDto(
                order.getId(),
                user.getId(),
                order.getStatus().name(),     // ✅ enum → String으로 변환
                totalPrice,
                itemResponses,
                order.getOrderDate()         // ✅ 마지막 파라미터!
        );
    }
    @Override
    public List<OrderResponseDto> getOrdersByUserId(Long userId) {
        return orderRepository.findByUserId(userId).stream()
                .map(order -> OrderResponseDto.builder()
                        .id(order.getId())
                        .userId(order.getUser().getId())
                        .orderDate(order.getOrderDate())
                        .status(order.getStatus().name())
                        .totalPrice(order.getTotalPrice())
                        .items(order.getItems().stream().map(item -> new OrderItemResponseDto(
                                item.getId(),
                                item.getMenu().getId(),
                                item.getStore().getId(),
                                item.getQuantity(),
                                item.getItemPrice()
                        )).collect(Collectors.toList()))
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public OrderResponseDto getOrderById(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new CustomException(
                        ErrorCode.ORDER_NOT_FOUND.getMessage(),
                        ErrorCode.ORDER_NOT_FOUND.getHttpStatus()
                ));

        return OrderResponseDto.builder()
                .id(order.getId())
                .userId(order.getUser().getId())
                .orderDate(order.getOrderDate())
                .status(order.getStatus().name())
                .totalPrice(order.getTotalPrice())
                .items(order.getItems().stream().map(item -> new OrderItemResponseDto(
                        item.getId(),
                        item.getMenu().getId(),
                        item.getStore().getId(),
                        item.getQuantity(),
                        item.getItemPrice()
                )).collect(Collectors.toList()))
                .build();
    }

    @Override
    public PriceResponseDto calculatePrice(OrderRequestDto requestDto) {
        int totalPrice = requestDto.getItems().stream()
                .mapToInt(item -> item.getItemPrice() * item.getQuantity())
                .sum();

        return new PriceResponseDto(totalPrice, totalPrice);
    }

    //주문(Order)의 상태(Status)를 변경
    @Override
    public OrderResponseDto updateOrderStatus(Long orderId, OrderStatusUpdateDto statusUpdateDto) {
        // 주문 ID로 주문 조회
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new CustomException(
                        ErrorCode.ORDER_NOT_FOUND.getMessage(),
                        ErrorCode.ORDER_NOT_FOUND.getHttpStatus()
                ));

        if (order.getStatus() == OrderStatus.COMPLETED) {
            throw new CustomException(
                    ErrorCode.CANNOT_CHANGE_COMPLETED_ORDER.getMessage(),
                    ErrorCode.CANNOT_CHANGE_COMPLETED_ORDER.getHttpStatus()
            );
        }

        order.changeStatus(OrderStatus.valueOf(statusUpdateDto.getStatus()));

        return OrderResponseDto.builder()
                .id(order.getId())
                .userId(order.getUser().getId())
                .orderDate(order.getOrderDate())
                .status(order.getStatus().name())
                .totalPrice(order.getTotalPrice())
                .items(order.getItems().stream().map(item -> new OrderItemResponseDto(
                        item.getId(),
                        item.getMenu().getId(),
                        item.getStore().getId(),
                        item.getQuantity(),
                        item.getItemPrice()
                )).toList())
                .build();
    }

    @Override
    public CancelResponseDto cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new CustomException(
                        ErrorCode.ORDER_NOT_FOUND.getMessage(),
                        ErrorCode.ORDER_NOT_FOUND.getHttpStatus()
                ));

        if (order.getStatus() == OrderStatus.CANCELLED || order.getStatus() == OrderStatus.COMPLETED) {
            throw new CustomException(
                    ErrorCode.CANNOT_CANCEL_COMPLETED_OR_CANCELED_ORDER.getMessage(),
                    ErrorCode.CANNOT_CANCEL_COMPLETED_OR_CANCELED_ORDER.getHttpStatus()
            );
        }

        orderRepository.delete(order);
        return new CancelResponseDto("주문이 성공적으로 취소되었습니다.", LocalDateTime.now());
    }
    }


