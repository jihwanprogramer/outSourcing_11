package com.example.outsourcing_11.domain.store.dto;

import lombok.Getter;

import com.example.outsourcing_11.domain.store.entity.Store;
import com.example.outsourcing_11.domain.store.entity.StoreCategory;
import com.example.outsourcing_11.domain.store.entity.StoreStatus;

/**
 * 리스트 다건 조회용
 */
@Getter
public class StoreListDto {
	private Long id;
	private final String name;
	private final int minimumOrderPrice;
	private final StoreStatus status;
	private final StoreCategory category;

	public StoreListDto(Store store) {
		this.id = store.getId();
		this.name = store.getName();
		this.minimumOrderPrice = store.getMinimumOrderPrice();
		this.status = store.getStatus();
		this.category = store.getCategory();
	}
}
