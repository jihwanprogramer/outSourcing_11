package com.example.outsourcing_11.domain.store.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import com.example.outsourcing_11.domain.store.entity.StoreCategory;
import com.example.outsourcing_11.domain.store.entity.StoreStatus;

/**
 * 가게 다건 조회용
 */
@Getter
@AllArgsConstructor
public class StoreListDto {
	private Long id;
	private String name;
	private int minimumOrderPrice;
	private StoreStatus status;
	private StoreCategory category;

}
