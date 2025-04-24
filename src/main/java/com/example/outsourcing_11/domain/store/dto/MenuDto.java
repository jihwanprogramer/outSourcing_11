package com.example.outsourcing_11.domain.store.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import com.example.outsourcing_11.domain.store.entity.Store;

@Getter
@RequiredArgsConstructor
public class MenuDto {
	private Long id;
	private String name;
	private int price;

	public MenuDto(Store store) {
		this.id = id;
		this.name = name;
		this.price = price;
	}
}
