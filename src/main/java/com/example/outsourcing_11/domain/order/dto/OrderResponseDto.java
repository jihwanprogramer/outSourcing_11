package com.example.outsourcing_11.domain.order.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class OrderResponseDto {
    private Long id;
    private Long userId;
    private String status;
    private int totalPrice;
    private List<OrderItemResponseDto> items;
    private LocalDateTime orderDate;

    public OrderResponseDto(Long id, Long userId, String status, int totalPrice,
                            List<OrderItemResponseDto> items, LocalDateTime orderDate) {
        this.id = id;
        this.userId = userId;
        this.status = status;
        this.totalPrice = totalPrice;
        this.items = items;
        this.orderDate = orderDate;
    }
}
