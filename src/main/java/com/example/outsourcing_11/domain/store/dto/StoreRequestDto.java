package com.example.outsourcing_11.domain.store.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;

import com.example.outsourcing_11.domain.store.entity.StoreCategory;

/**
 * 요청 데이터 담는 dto
 */
@Getter
@AllArgsConstructor
public class StoreRequestDto {
	private String name;
	private LocalDateTime openTime;
	private LocalDateTime closeTime;
	private int minOrderPrice;
	private String address;
	private StoreCategory category;

}
