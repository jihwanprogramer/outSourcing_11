package com.example.outsourcing_11.domain.order.service;

import com.example.outsourcing_11.domain.order.dto.*;

import java.util.List;

public interface OrderService {

    // 주문 생성
    OrderResponseDto createOrder(OrderRequestDto requestDto);

    // 특정 사용자 주문 목록 조회
    List<OrderResponseDto> getOrdersByUserId(Long userId);

    // 단일 주문 조회
    OrderResponseDto getOrderById(Long orderId);

    // 주문 가격 계산 (포인트/쿠폰 반영 X)
    PriceResponseDto calculatePrice(OrderRequestDto requestDto);

    // 주문 상태 변경
    OrderResponseDto updateOrderStatus(Long orderId, OrderStatusUpdateDto statusUpdateDto);

    // 주문 취소
    CancelResponseDto cancelOrder(Long orderId);


}
