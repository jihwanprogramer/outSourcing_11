package com.example.outsourcing_11.domain.store.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class MenuDto {
	private Long id;
	private String name;
	private int price;

	public MenuDto(String name, int price) {
		this.name = name;
		this.price = price;
	}
}
