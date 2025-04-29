package com.example.outsourcing_11.domain.store.dto;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class SalesDto {
    private BigDecimal totalSales;
    private Long totalCustomers;


    public SalesDto(BigDecimal totalSales, Long totalCustomers) {
        this.totalSales = totalSales;
        this.totalCustomers = totalCustomers;
    }
}
