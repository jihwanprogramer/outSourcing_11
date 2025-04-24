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

}
