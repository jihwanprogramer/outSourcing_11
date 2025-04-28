package com.example.outsourcing_11.domain.store.dto;

import java.math.BigDecimal;

import lombok.Getter;

@Getter
public class SalesDto {
	private BigDecimal totalSales;

	public SalesDto(BigDecimal totalSales) {
		this.totalSales = totalSales;
	}
}
