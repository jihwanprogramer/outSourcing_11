package com.example.outsourcing_11.domain.order.controller;

import com.example.outsourcing_11.domain.order.dto.OrderRequestDto;
import com.example.outsourcing_11.domain.order.dto.OrderResponseDto;
import com.example.outsourcing_11.domain.order.service.OrderService;
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
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<OrderResponseDto>> getOrdersByUserId(@PathVariable Long userId) {
        List<OrderResponseDto> orders = orderService.getOrdersByUserId(userId);
        return ResponseEntity.ok(orders);
    }
}

