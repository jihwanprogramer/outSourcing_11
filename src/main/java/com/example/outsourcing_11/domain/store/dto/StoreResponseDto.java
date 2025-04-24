package com.example.outsourcing_11.domain.store.dto;

public class StoreResponseDto {
	private Long id;
	private String name;
	private String openTime;
	private String closeTime;
	private int minimumOrderPrice;

	public StoreResponseDto(Long id, String name, String openTime, String closeTime, int minimumOrderPrice) {
		this.id = id;
		this.name = name;
		this.openTime = openTime;
		this.closeTime = closeTime;
		this.minimumOrderPrice = minimumOrderPrice;
	}
}
