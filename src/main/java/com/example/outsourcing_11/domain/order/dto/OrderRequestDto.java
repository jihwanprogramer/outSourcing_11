package com.example.outsourcing_11.domain.order.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequestDto {
    private Long userId;
    private List<OrderItemRequestDto> items;
}
