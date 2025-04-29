package com.example.outsourcing_11.domain.store.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import com.example.outsourcing_11.domain.store.entity.Store;
import com.example.outsourcing_11.domain.store.entity.StoreCategory;
import com.example.outsourcing_11.domain.store.entity.StoreStatus;

/**
 * 가게 다건 조회용
 */
@Getter
@RequiredArgsConstructor
public class StoreListDto {
	private Long id;
	private String name;
	private int minOrderPrice;
	private StoreStatus status;
	private StoreCategory category;
	private int favoriteCount;

	public StoreListDto(Store store) {
		this.id = store.getId();
		this.name = store.getName();
		this.category = store.getCategory();
		this.minOrderPrice = store.getMinimumOrderPrice();
		this.status = store.getStatus();
		this.favoriteCount = store.getFavoriteCount();
	}
}
