package com.example.outsourcing_11.domain.order.service;

import com.example.outsourcing_11.domain.order.dto.CartItemRequestDto;
import com.example.outsourcing_11.domain.order.dto.CartRequestDto;
import com.example.outsourcing_11.domain.order.dto.CartResponseDto;
import com.example.outsourcing_11.domain.order.entity.Cart;

public interface CartService {
    CartResponseDto getCartByUserId(Long userId);
    CartResponseDto createCart(CartRequestDto requestDto);
    CartResponseDto addItemToCart(CartItemRequestDto dto);

    Cart getEntityByUserId(Long userId);
}
