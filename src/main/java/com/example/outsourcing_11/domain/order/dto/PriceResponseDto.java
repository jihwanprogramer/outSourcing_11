package com.example.outsourcing_11.domain.order.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PriceResponseDto {

    private int originalPrice;
    private int finalPrice;

}
