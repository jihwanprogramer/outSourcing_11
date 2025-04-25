package com.example.outsourcing_11.domain.order.controller;

import com.example.outsourcing_11.domain.order.dto.*;
import com.example.outsourcing_11.domain.order.service.OrderService;
import com.example.outsourcing_11.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {


    private final OrderService orderService;
    /**
     * 주문 생성 요청 처리
     */
    @PostMapping
    public ResponseEntity<OrderResponseDto> createOrder(@RequestBody OrderRequestDto requestDto) {
        OrderResponseDto createdOrder = orderService.createOrder(requestDto);
        return ResponseEntity.ok(createdOrder);
    }

    /**
     * 특정 사용자의 주문 목록 조회
     */
    @GetMapping("/user/{id}")
    public ResponseEntity<List<OrderResponseDto>> getOrdersByUserId(@PathVariable("id") Long id) {
        return ResponseEntity.ok(orderService.getOrdersByUserId(id));
    }

    /**
     * 단일 주문 조회
     */
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponseDto> getOrder(@PathVariable Long orderId) {
        return ResponseEntity.ok(orderService.getOrderById(orderId));

    }

    /**
     * 주문 가격 계산
     */
    @PostMapping("/price")
    public ResponseEntity<PriceResponseDto> calculatePrice(@RequestBody OrderRequestDto requestDto) {
        return ResponseEntity.ok(orderService.calculatePrice(requestDto));
    }

    /**
     * 주문 상태 변경
     */
    @PatchMapping("/{orderId}")
    public ResponseEntity<OrderResponseDto> updateOrderStatus(
            @PathVariable("orderId") Long orderId, // 여기 "orderId" 명시!!
            @RequestBody OrderStatusUpdateDto requestDto) {
        return ResponseEntity.ok(orderService.updateOrderStatus(orderId, requestDto));
    }

    /**
     * 주문 취소
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<CancelResponseDto> cancelOrder(@PathVariable("id") Long id) {
        return ResponseEntity.ok(orderService.cancelOrder(id)); // ✅ 한 번만 호출
    }
}



