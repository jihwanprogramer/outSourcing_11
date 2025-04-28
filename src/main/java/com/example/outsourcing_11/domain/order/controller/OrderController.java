package com.example.outsourcing_11.domain.order.controller;

import com.example.outsourcing_11.common.exception.CustomException;
import com.example.outsourcing_11.common.exception.ErrorCode;
import com.example.outsourcing_11.domain.order.dto.*;
import com.example.outsourcing_11.domain.order.service.OrderService;
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
        OrderResponseDto createdOrder = orderService.createOrder(requestDto);
        if (createdOrder == null) {
            throw new CustomException(ErrorCode.INVALID_MENU_IN_ORDER);
        }
        return new ResponseEntity<>(createdOrder, HttpStatus.CREATED);
    }

    /**
     * 특정 사용자의 주문 목록 조회
     */
    @GetMapping("/user/{id}")
    public ResponseEntity<List<OrderResponseDto>> getOrdersByUserId(@PathVariable("id") Long id) {
        List<OrderResponseDto> orders = orderService.getOrdersByUserId(id);
        return ResponseEntity.ok(orderService.getOrdersByUserId(id));
    }

    /**
     * 단일 주문 조회
     */
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponseDto> getOrder(@PathVariable Long orderId) {
        OrderResponseDto order = orderService.getOrderById(orderId);
        if (order == null) {
            throw new CustomException(ErrorCode.ORDER_NOT_FOUND);
        }
        return new ResponseEntity<>(order, HttpStatus.OK);

    }

    /**
     * 주문 가격 계산
     */
    @PostMapping("/price")
    public ResponseEntity<PriceResponseDto> calculatePrice(@RequestBody OrderRequestDto requestDto) {
        PriceResponseDto price = orderService.calculatePrice(requestDto);
        return new ResponseEntity<>(price, HttpStatus.OK);
    }

    /**
     * 주문 상태 변경
     */
    @PatchMapping("/{orderId}")
    public ResponseEntity<OrderResponseDto> updateOrderStatus(
        @PathVariable("orderId") Long orderId, // 여기 "orderId" 명시!!
        @RequestBody OrderStatusUpdateDto requestDto) {
        OrderResponseDto updatedOrder = orderService.updateOrderStatus(orderId, requestDto);
        if (updatedOrder == null) {
            throw new CustomException(ErrorCode.CANNOT_CHANGE_COMPLETED_ORDER);
        }
        return new ResponseEntity<>(updatedOrder, HttpStatus.OK);
    }

    /**
     * 주문 취소
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<CancelResponseDto> cancelOrder(@PathVariable("id") Long id) {
        CancelResponseDto cancelResponse = orderService.cancelOrder(id);
        if (cancelResponse == null) {
            throw new CustomException(ErrorCode.CANNOT_CANCEL_COMPLETED_OR_CANCELED_ORDER);
        }
        return new ResponseEntity<>(cancelResponse, HttpStatus.OK);
    }
}



