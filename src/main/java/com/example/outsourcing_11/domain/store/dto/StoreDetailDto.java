package com.example.outsourcing_11.domain.store.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

import com.example.outsourcing_11.domain.store.entity.Store;
import com.example.outsourcing_11.domain.store.entity.StoreStatus;

/**
 * 가게 단건조회( 메뉴리스트, 공지까지 포함)
 */
@Getter
@AllArgsConstructor
public class StoreDetailDto {
	private Long id;
	private String name;
	private LocalDateTime openTime;
	private LocalDateTime closeTime;
	private int minimumOrderPrice;
	private String notice;
	private StoreStatus status;
	private List<MenuDto> menus;

	public StoreDetailDto(Store store, List<MenuDto> menus) {
		this.id = store.getId();
		this.name = store.getName();
		this.openTime = store.getOpenTime();
		this.closeTime = store.getCloseTime();
		this.minimumOrderPrice = store.getMinimumOrderPrice();
		this.notice = store.getNotice();

	}

}
