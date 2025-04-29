package com.example.outsourcing_11.domain.order.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

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


    @JsonCreator
    public CartRequestDto(@JsonProperty("userId") Long userId) {
        this.userId = userId;
    }


}
