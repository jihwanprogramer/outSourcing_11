package com.example.outsourcing_11.domain.order.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class OrderItemResponseDto {
    private Long id;
    private Long menuId;
    private Long storeId;
    private int quantity;
    private int itemPrice;


}
