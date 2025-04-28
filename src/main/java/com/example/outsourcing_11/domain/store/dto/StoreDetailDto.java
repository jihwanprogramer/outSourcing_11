package com.example.outsourcing_11.domain.store.dto;

import java.time.LocalTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

import com.example.outsourcing_11.domain.menu.dto.response.MenuUserResponseDto;
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
	private LocalTime openTime;
	private LocalTime closeTime;
	private List<NoticeDto> notices;
	private int minimumOrderPrice;
	private StoreStatus status;
	private List<MenuUserResponseDto> menus;

	public StoreDetailDto(Store store, List<MenuUserResponseDto> menus) {
		this.id = store.getId();
		this.name = store.getName();
		this.openTime = store.getOpenTime();
		this.closeTime = store.getCloseTime();
		this.minimumOrderPrice = store.getMinimumOrderPrice();
		this.status = store.getStatus();
		this.notices = store.getNotices().stream()
			.map(notice -> new NoticeDto(
				notice.getContent()
			))
			.toList();
		this.menus = menus;

	}

}
