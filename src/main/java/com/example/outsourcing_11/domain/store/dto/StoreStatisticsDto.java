package com.example.outsourcing_11.domain.store.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class StoreStatisticsDto {
	private long customerCount;
	private long totalSales;
}
