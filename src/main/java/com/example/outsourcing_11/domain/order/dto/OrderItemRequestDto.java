package com.example.outsourcing_11.domain.order.dto;

import com.example.outsourcing_11.domain.menu.entity.Menu;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemRequestDto {
    private Long menuId;
    private Long storeId;
    private int quantity;
    private int itemPrice;
}


