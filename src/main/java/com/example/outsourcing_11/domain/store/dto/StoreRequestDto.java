package com.example.outsourcing_11.domain.store.dto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;

import com.example.outsourcing_11.domain.store.entity.StoreCategory;

/**
 * 요청 데이터 담는 dto
 */
@Getter
@NoArgsConstructor
public class StoreRequestDto {
	private String name;
	private LocalDateTime openTime;
	private LocalDateTime closeTime;
	private int minimumOrderPrice;
	private String address;
	private StoreCategory category;
}
