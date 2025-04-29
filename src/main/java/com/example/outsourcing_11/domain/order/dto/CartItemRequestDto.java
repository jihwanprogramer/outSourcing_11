package com.example.outsourcing_11.domain.order.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class CartItemRequestDto {
    @NotNull(message = "사용자 ID는 필수입니다.")
    private Long userId;

    @NotNull(message = "메뉴 ID는 필수입니다.")
    private Long menuId;

    @NotNull(message = "스토어 ID는 필수입니다.")
    private Long storeId;

    @Min(value = 1, message = "수량은 1개 이상이어야 합니다.")
    private int quantity;

    @Builder
    public CartItemRequestDto(Long userId, Long menuId, Long storeId, int quantity) {
        this.userId = userId;
        this.menuId = menuId;
        this.storeId = storeId;
        this.quantity = quantity;
    }
}
