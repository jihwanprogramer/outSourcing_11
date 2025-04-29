package com.example.outsourcing_11.domain.order.controller;

import com.example.outsourcing_11.common.exception.CustomException;
import com.example.outsourcing_11.common.exception.ErrorCode;
import com.example.outsourcing_11.domain.order.dto.*;
import com.example.outsourcing_11.domain.order.entity.Cart;
import com.example.outsourcing_11.domain.order.service.CartService;
import com.example.outsourcing_11.domain.order.service.OrderService;
import jakarta.validation.Valid;
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
        return new ResponseEntity<>(cartService.getCartByUserId(userId), HttpStatus.OK);
    }

    /**
     * 새로운 장바구니 생성
     */
    @PostMapping
    public ResponseEntity<CartResponseDto> createCart(@RequestBody @Valid CartRequestDto requestDto) {
        return new ResponseEntity<>(cartService.createCart(requestDto), HttpStatus.CREATED);
    }

    /**
     * 장바구니에 항목 추가
     */
    @PostMapping("/items")
    public ResponseEntity<CartResponseDto> addItemToCart(@RequestBody @Valid CartItemRequestDto dto) {
        return new ResponseEntity<>(cartService.addItemToCart(dto), HttpStatus.CREATED);
    }

    @PostMapping("/{userId}/checkout")
    public ResponseEntity<OrderResponseDto> checkoutCart(@PathVariable Long userId) {
        Cart cart = cartService.getEntityByUserId(userId);

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
