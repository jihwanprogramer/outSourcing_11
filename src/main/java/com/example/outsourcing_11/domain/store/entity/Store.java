package com.example.outsourcing_11.domain.store.entity;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import com.example.outsourcing_11.common.Base;
import com.example.outsourcing_11.common.Status;
import com.example.outsourcing_11.domain.menu.entity.Menu;
import com.example.outsourcing_11.domain.order.entity.Order;
import com.example.outsourcing_11.domain.store.dto.StoreRequestDto;
import com.example.outsourcing_11.domain.user.entity.User;

@Getter
@Entity
@RequiredArgsConstructor
public class Store extends Base {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, length = 100)
	private String name;

	@Column(name = "open_time", nullable = false)
	private LocalDateTime openTime;

	@Column(name = "close_time", nullable = false)
	private LocalDateTime closeTime;

	@Column(name = "minimum_order_price", nullable = false)
	private int minimumOrderPrice;

	@Column(nullable = false, length = 255)
	private String address;

	@Column(columnDefinition = "TEXT")
	private String notice;

	@Enumerated(EnumType.STRING)
	private StoreStatus status = StoreStatus.OPEN;

	@Enumerated(EnumType.STRING)
	private StoreCategory category;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(referencedColumnName = "user_id", name = "owner_id")
	private User owner;

	@OneToMany(mappedBy = "store", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Menu> menus;

	@OneToMany(mappedBy = "store", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Order> orders;

	@OneToMany(mappedBy = "store", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Notice> notices;

	@OneToMany(mappedBy = "store", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Favorite> favorites;

	@Column(columnDefinition = "TINYINT(1) DEFAULT 1")
	private boolean deleted = Status.EXIST.getValue();

	public Store(StoreRequestDto dto, User user) {
		this.name = dto.getName();
		this.address = dto.getAddress();
		this.openTime = dto.getOpenTime();
		this.closeTime = dto.getCloseTime();
		this.minimumOrderPrice = dto.getMinOrderPrice();
		this.status = dto.getStatus();
		this.category = dto.getCategory();
		this.owner = user;
	}

	public Store(String name, String address, LocalDateTime openTime, LocalDateTime closeTime, int price,
		StoreStatus status, StoreCategory storeCategory, User user) {
		this.name = name;
		this.address = address;
		this.openTime = openTime;
		this.closeTime = closeTime;
		this.minimumOrderPrice = price;
		this.status = status;
		this.category = storeCategory;
		this.owner = user;
	}

	public void updateStatus() {
		LocalDateTime now = LocalDateTime.now();
		if (now.isAfter(this.openTime) && now.isBefore(this.closeTime)) {
			this.status = StoreStatus.OPEN;
		} else {
			this.status = StoreStatus.CLOSED;
		}
	}

	public void update(StoreRequestDto requestDto) {
		this.name = requestDto.getName();
		this.openTime = requestDto.getOpenTime();
		this.closeTime = requestDto.getCloseTime();
		this.minimumOrderPrice = requestDto.getMinOrderPrice();
	}

	public void softDelete() {
		this.deletedAt = LocalDateTime.now();
		this.deleted = Status.NON_EXIST.getValue();
	}

}
