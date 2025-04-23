package com.example.outsourcing_11.domain.store.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class StoreRequestDto {
	private String name;
	private String openTime;
	private String closeTime;
	private int minimumOrderPrice;

}
