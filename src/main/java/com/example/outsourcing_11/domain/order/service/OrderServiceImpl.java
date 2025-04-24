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
        User user = userRepository.findById(requestDto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Order order = new Order();
        order.setUser(user);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(OrderStatus.PENDING);

        List<OrderItem> items = new ArrayList<>();
        List<OrderItemResponseDto> itemResponses = new ArrayList<>();
        int totalPrice = 0;

        for (OrderItemRequestDto dto : requestDto.getItems()) {
            OrderItem item = new OrderItem();
            item.setMenu(menuRepository.findById(dto.getMenuId())
                    .orElseThrow(() -> new RuntimeException("Menu not found")));
            item.setStore(storeRepository.findById(dto.getStoreId())
                    .orElseThrow(() -> new RuntimeException("Store not found")));
            item.setQuantity(dto.getQuantity());
            item.setItemPrice(dto.getItemPrice());
            item.setOrder(order);
            items.add(item);

            totalPrice += dto.getItemPrice() * dto.getQuantity();

            OrderItemResponseDto.builder()
                    .id(null)
                    .menuId(dto.getMenuId())
                    .storeId(dto.getStoreId())
                    .quantity(dto.getQuantity())
                    .itemPrice(dto.getItemPrice())
                    .build();
        }

        order.setTotalPrice(totalPrice);
        order.getItems().addAll(items);

        Order saved = orderRepository.save(order);

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
        // 실제 구현 내용{
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        order.setStatus(OrderStatus.valueOf(statusUpdateDto.getStatus()));
        Order updated = orderRepository.save(order);

        return OrderResponseDto.builder()
                .id(updated.getId())
                .userId(updated.getUser().getId())
                .orderDate(updated.getOrderDate())
                .status(updated.getStatus().name())
                .totalPrice(updated.getTotalPrice())
                .items(updated.getItems().stream().map(item -> new OrderItemResponseDto(
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

        order.setStatus(OrderStatus.CANCELLED);
        orderRepository.save(order);

        return new CancelResponseDto("주문이 성공적으로 취소되었습니다.", LocalDateTime.now());
    }
}
