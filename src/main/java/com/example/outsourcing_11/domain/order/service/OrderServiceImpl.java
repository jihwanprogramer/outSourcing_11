package com.example.outsourcing_11.domain.order.service;

import com.example.outsourcing_11.domain.menu.repository.MenuRepository;
import com.example.outsourcing_11.domain.order.dto.*;
import com.example.outsourcing_11.domain.order.entity.Order;
import com.example.outsourcing_11.domain.order.entity.OrderItem;
import com.example.outsourcing_11.domain.order.entity.OrderStatus;
import com.example.outsourcing_11.domain.order.repository.OrderRepository;
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
        // 사용자 정보 조회
        User user = userRepository.findById(requestDto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<OrderItem> items = new ArrayList<>(); // 주문 항목 리스트
        List<OrderItemResponseDto> itemResponses = new ArrayList<>(); // 응답용 DTO 리스트
        int totalPrice = 0;  // 총 주문 가격 계산용

        // 요청된 각 주문 항목 처리
        for (OrderItemRequestDto dto : requestDto.getItems()) {
            OrderItem item = new OrderItem();
            item.setMenu(menuRepository.findById(dto.getMenuId())
                    .orElseThrow(() -> new RuntimeException("Menu not found")));
            item.setStore(storeRepository.findById(dto.getStoreId())
                    .orElseThrow(() -> new RuntimeException("Store not found")));
            item.setQuantity(dto.getQuantity());
            item.setItemPrice(dto.getItemPrice());

            // 총 주문 금액 계산
            totalPrice += dto.getItemPrice() * dto.getQuantity();
            items.add(item);

            // 응답용 DTO 생성
            itemResponses.add(OrderItemResponseDto.builder()
                    .id(null)
                    .menuId(dto.getMenuId())
                    .storeId(dto.getStoreId())
                    .quantity(dto.getQuantity())
                    .itemPrice(dto.getItemPrice())
                    .build());
        }

// ✅ 여기서 totalPrice 계산 완료 후 생성
        // 주문 객체 생성 (생성자에서 totalPrice까지 포함)
        Order order = new Order(user, LocalDateTime.now(), OrderStatus.PENDING, totalPrice, items);

        // 각 주문 항목에 주문 연결
        for (OrderItem item : items) {
            item.setOrder(order);
        }

        // 주문에 항목들 추가
        order.getItems().addAll(items);

        // DB에 주문 저장
        Order saved = orderRepository.save(order);

        // 저장된 주문 정보를 기반으로 응답 DTO 구성
        return OrderResponseDto.builder()
                .id(saved.getId())
                .userId(saved.getUser().getId())
                .orderDate(saved.getOrderDate())
                .status(saved.getStatus().name())
                .totalPrice(saved.getTotalPrice())
                .items(itemResponses)
                .build();
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
                .orElseThrow(() -> new RuntimeException("Order not found"));

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

    @Override
    public OrderResponseDto updateOrderStatus(Long orderId, OrderStatusUpdateDto statusUpdateDto) {
        // 주문 ID로 주문 조회
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        // 새로운 상태로 주문 객체를 새로 생성
        Order updated = new Order(
                order.getUser(),
                order.getOrderDate(),
                OrderStatus.valueOf(statusUpdateDto.getStatus()),
                order.getTotalPrice(),
                order.getItems()
        );

        Order saved = orderRepository.save(updated);

        return OrderResponseDto.builder()
                .id(saved.getId())
                .userId(saved.getUser().getId())
                .orderDate(saved.getOrderDate())
                .status(saved.getStatus().name())
                .totalPrice(saved.getTotalPrice())
                .items(saved.getItems().stream().map(item -> new OrderItemResponseDto(
                        item.getId(),
                        item.getMenu().getId(),
                        item.getStore().getId(),
                        item.getQuantity(),
                        item.getItemPrice()
                )).collect(Collectors.toList()))
                .build();
    }

    @Override
    public CancelResponseDto cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        // 주문 취소 상태로 새 객체 생성
        Order cancelled = new Order(
                order.getUser(),
                order.getOrderDate(),
                OrderStatus.CANCELLED,
                order.getTotalPrice(),
                order.getItems()
        );

        orderRepository.save(cancelled);

        return new CancelResponseDto("주문이 성공적으로 취소되었습니다.", LocalDateTime.now());
    }

}
