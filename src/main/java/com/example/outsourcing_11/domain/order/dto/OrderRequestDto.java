package com.example.outsourcing_11.domain.order.dto;

import java.util.List;

public class OrderRequestDto {
    private Long userId;
    private List<OrderItemRequestDto> items;

    public Long getUserId() {
        return userId;
    }

    public List<OrderItemRequestDto> getItems() {
        return items;
    }
}
