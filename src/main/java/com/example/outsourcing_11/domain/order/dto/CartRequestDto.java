package com.example.outsourcing_11.domain.order.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class CartRequestDto {
    @NotNull(message = "사용자 ID는 필수입니다.")
    private Long userId;

    private List<@Valid CartItemRequestDto> items; // (참고) 실제 사용 안 하면 삭제해도 됨

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
