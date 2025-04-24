package com.example.outsourcing_11.domain.store.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

import com.example.outsourcing_11.domain.store.entity.StoreStatus;

/**
 * 가게 단건조회( 메뉴리스트, 공지까지 포함)
 */
@Getter
@AllArgsConstructor
public class StoreDetailDto {
	private Long id;
	private String name;
	private String openTime;
	private String closeTime;
	private int minOrderPrice;
	private String notice;
	private StoreStatus status;
	private List<MenuDto> menus;
}
