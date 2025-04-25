package com.example.outsourcing_11.domain.order.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class CartItemRequestDto {
    private Long userId;
    private Long menuId;
    private Long storeId;
    private int quantity;

    @Builder
    public CartItemRequestDto(Long userId, Long menuId, Long storeId, int quantity) {
        this.userId = userId;
        this.menuId = menuId;
        this.storeId = storeId;
        this.quantity = quantity;
    }
}
