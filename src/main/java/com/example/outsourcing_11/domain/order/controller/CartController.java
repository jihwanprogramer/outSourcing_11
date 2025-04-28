package com.example.outsourcing_11.domain.order.controller;

import com.example.outsourcing_11.common.exception.CustomException;
import com.example.outsourcing_11.common.exception.ErrorCode;
import com.example.outsourcing_11.domain.order.dto.*;
import com.example.outsourcing_11.domain.order.entity.Cart;
import com.example.outsourcing_11.domain.order.service.CartService;
import com.example.outsourcing_11.domain.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/carts")
public class CartController {

    private final CartService cartService;
    private final OrderService orderService;

    /**
     * 사용자 ID로 장바구니 조회
     */
    @GetMapping("/{userId}")
    public ResponseEntity<CartResponseDto> getCartByUserId(@PathVariable Long userId) {
        CartResponseDto cart = cartService.getCartByUserId(userId);
        if (cart == null || cart.getItems().isEmpty()) {
            throw new CustomException(ErrorCode.CART_EMPTY);

        }
        return ResponseEntity.ok(cart);
    }

    /**
     * 새로운 장바구니 생성
     */
    @PostMapping
    public ResponseEntity<CartResponseDto> createCart(@RequestBody CartRequestDto requestDto) {
        CartResponseDto createdCart = cartService.createCart(requestDto);
        if (createdCart == null) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }
        return new ResponseEntity<>(createdCart, HttpStatus.OK);
    }

    /**
     * 장바구니에 항목 추가
     */
    @PostMapping("/items")
    public ResponseEntity<CartResponseDto> addItemToCart(@RequestBody CartItemRequestDto dto) {
        if (dto.getQuantity() <= 0) {
            throw new CustomException(ErrorCode.INVALID_QUANTITY);
        }

        CartResponseDto responseDto = cartService.addItemToCart(dto);
        if (responseDto == null) {
        }
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    @PostMapping("/{userId}/checkout")
    public ResponseEntity<OrderResponseDto> checkoutCart(@PathVariable Long userId) {
        // 1. 장바구니 가져오기
        Cart cart = cartService.getEntityByUserId(userId);
        if (cart == null || cart.getItems().isEmpty()) {
            throw new CustomException(ErrorCode.CART_EMPTY);
        }

        // 2. 장바구니의 아이템들을 OrderItemRequestDto 리스트로 변환
        List<OrderItemRequestDto> orderItems = cart.getItems().stream()
            .map(item -> new OrderItemRequestDto(
                item.getMenu().getId(),
                item.getStore().getId(),
                item.getQuantity(),
                item.getMenu().getPrice().intValue()
            ))
            .collect(Collectors.toList());

        // 3. OrderRequestDto 만들고 주문 생성
        OrderRequestDto orderRequest = new OrderRequestDto(userId, orderItems);
        OrderResponseDto orderResponse = orderService.createOrder(orderRequest);

        // 4. 응답
        return new ResponseEntity<>(orderResponse, HttpStatus.CREATED);
    }
}
