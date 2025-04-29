package com.example.outsourcing_11.domain.store.dto;

import java.time.LocalTime;

import lombok.AllArgsConstructor;
import lombok.Getter;

import com.example.outsourcing_11.domain.store.entity.StoreCategory;
import com.example.outsourcing_11.domain.store.entity.StoreStatus;

/**
 * 요청 데이터 담는 dto
 */
@Getter
@AllArgsConstructor
public class StoreRequestDto {
	private String name;
	private LocalTime openTime;
	private LocalTime closeTime;
	private int minOrderPrice;
	private String address;
	private StoreCategory category;
	private StoreStatus status;

	public StoreRequestDto() {

	}
}
