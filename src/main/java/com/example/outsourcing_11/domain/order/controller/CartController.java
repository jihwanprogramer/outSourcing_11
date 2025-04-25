package com.example.outsourcing_11.domain.order.controller;

import com.example.outsourcing_11.domain.order.dto.CartItemRequestDto;
import com.example.outsourcing_11.domain.order.dto.CartRequestDto;
import com.example.outsourcing_11.domain.order.dto.CartResponseDto;
import com.example.outsourcing_11.domain.order.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/carts")
public class CartController {

    private final CartService cartService;
    /**
     * 사용자 ID로 장바구니 조회
     */
    @GetMapping("/{userId}")
    public ResponseEntity<CartResponseDto> getCartByUserId(@PathVariable Long userId) {
        CartResponseDto cart = cartService.getCartByUserId(userId);
        return ResponseEntity.ok(cart);
    }

    /**
     * 새로운 장바구니 생성
     */
    @PostMapping
    public ResponseEntity<CartResponseDto> createCart(@RequestBody CartRequestDto requestDto) {
        CartResponseDto createdCart = cartService.createCart(requestDto);
        return ResponseEntity.ok(createdCart);
    }
    /**
     * 장바구니에 항목 추가
     */
    @PostMapping("/items")
    public ResponseEntity<CartResponseDto> addItemToCart(@RequestBody CartItemRequestDto dto) {
        CartResponseDto responseDto = cartService.addItemToCart(dto);
        return ResponseEntity.ok(responseDto);
    }
}
