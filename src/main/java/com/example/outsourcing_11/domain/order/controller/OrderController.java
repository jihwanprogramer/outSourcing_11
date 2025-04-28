package com.example.outsourcing_11.domain.order.controller;

import com.example.outsourcing_11.common.exception.CustomException;
import com.example.outsourcing_11.common.exception.ErrorCode;
import com.example.outsourcing_11.domain.order.dto.*;
import com.example.outsourcing_11.domain.order.service.OrderService;
import com.example.outsourcing_11.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
        return new ResponseEntity<>(orderService.createOrder(requestDto), HttpStatus.CREATED);
    }

    /**
     * 특정 사용자의 주문 목록 조회
     */
    @GetMapping("/user/{id}")
    public ResponseEntity<List<OrderResponseDto>> getOrdersByUserId(@PathVariable("id") Long id) {
        return new ResponseEntity<>(orderService.getOrdersByUserId(id), HttpStatus.OK);
    }

    /**
     * 단일 주문 조회
     */
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponseDto> getOrder(@PathVariable Long orderId) {
        return new ResponseEntity<>(orderService.getOrderById(orderId), HttpStatus.OK);

    }

    /**
     * 주문 가격 계산
     */
    @PostMapping("/price")
    public ResponseEntity<PriceResponseDto> calculatePrice(@RequestBody OrderRequestDto requestDto) {
        return new ResponseEntity<>(orderService.calculatePrice(requestDto), HttpStatus.OK);
    }

    /**
     * 주문 상태 변경
     */
    @PatchMapping("/{orderId}")
    public ResponseEntity<OrderResponseDto> updateOrderStatus(
            @PathVariable("orderId") Long orderId, // 여기 "orderId" 명시!!
            @RequestBody OrderStatusUpdateDto requestDto) {
        return new ResponseEntity<>(orderService.updateOrderStatus(orderId, requestDto), HttpStatus.OK);
    }

    /**
     * 주문 취소
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<CancelResponseDto> cancelOrder(@PathVariable("id") Long id) {
        return new ResponseEntity<>(orderService.cancelOrder(id), HttpStatus.OK);
    }
}



