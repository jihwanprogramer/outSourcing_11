package com.example.outsourcing_11.domain.store.dto;

import java.time.LocalTime;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import com.example.outsourcing_11.domain.store.entity.Store;
import com.example.outsourcing_11.domain.store.entity.StoreCategory;
import com.example.outsourcing_11.domain.store.entity.StoreStatus;

/**
 * 응답 데이터 담은 dto
 */
@Getter
@RequiredArgsConstructor
public class StoreResponseDto {
	private Long id;
	private String name;
	private String address;
	private LocalTime openTime;
	private LocalTime closeTime;
	private int minimumOrderPrice;
	private StoreStatus status;
	private StoreCategory category;
	private int favoriteCount;

	public StoreResponseDto(Store store) {
		this.id = store.getId();
		this.name = store.getName();
		this.address = store.getAddress();
		this.openTime = store.getOpenTime();
		this.closeTime = store.getCloseTime();
		this.minimumOrderPrice = store.getMinimumOrderPrice();
		this.status = store.getStatus();
		this.category = store.getCategory();
		this.favoriteCount = store.getFavorites().size();
	}

}

