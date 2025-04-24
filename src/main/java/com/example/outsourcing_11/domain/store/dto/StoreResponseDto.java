package com.example.outsourcing_11.domain.store.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;

import com.example.outsourcing_11.domain.store.entity.StoreStatus;

/**
 * 응답 데이터 담은 dto
 */
@Getter
@AllArgsConstructor
public class StoreResponseDto {
	private Long id;
	private String name;
	private String address;
	private LocalDateTime openTime;
	private LocalDateTime closeTime;
	private int minimumOrderPrice;
	private StoreStatus status;

}
