package com.example.outsourcing_11.domain.order.dto;

import java.util.List;

public class CartRequestDto {
    private Long userId;
    private List<CartItemRequestDto> items;

    public Long getUserId() {
        return userId;
    }

    public List<CartItemRequestDto> getItems() {
        return items;
    }



}
