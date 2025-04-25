package com.example.outsourcing_11.domain.order.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
public class CartItemResponseDto {
    private Long id;
    private Long menuId;
    private Long storeId;
    private int quantity;

    public CartItemResponseDto(Long id, Long menuId, Long storeId, int quantity) {
        this.id = id;
        this.menuId = menuId;
        this.storeId = storeId;
        this.quantity = quantity;
    }
}
