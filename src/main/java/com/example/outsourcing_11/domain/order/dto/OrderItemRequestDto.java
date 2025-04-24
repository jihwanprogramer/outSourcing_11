package com.example.outsourcing_11.domain.order.dto;

import com.example.outsourcing_11.domain.menu.entity.Menu;

public class OrderItemRequestDto {
    private Long menuId;
    private Long storeId;
    private int quantity;
    private int itemPrice;

    public Long getMenuId() {
        return menuId;
    }

    public Long getStoreId() {
        return storeId;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getItemPrice() {
        return itemPrice;
    }

}
