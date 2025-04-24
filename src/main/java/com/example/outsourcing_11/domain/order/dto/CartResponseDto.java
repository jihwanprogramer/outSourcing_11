package com.example.outsourcing_11.domain.order.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class CartResponseDto {
    private Long id;
    private Long userId;
    private List<CartItemResponseDto> items;

    public CartResponseDto(Long id, Long userId, List<CartItemResponseDto> items) {
        this.id = id;
        this.userId = userId;
        this.items = items;
    }
}
