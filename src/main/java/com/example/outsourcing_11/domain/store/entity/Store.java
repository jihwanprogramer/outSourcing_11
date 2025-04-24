package com.example.outsourcing_11.domain.store.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import com.example.outsourcing_11.common.Base;
import com.example.outsourcing_11.domain.user.entity.User;

@Getter
@Entity
@NoArgsConstructor
public class Store extends Base {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;
	private LocalDateTime openTime;
	private LocalDateTime closeTime;
	private int minimumOrderPrice;
	private String address;

	@Enumerated(EnumType.STRING)
	private StoreStatus status = StoreStatus.OPEN;

	@Enumerated(EnumType.STRING)
	private StoreCategory category;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(referencedColumnName = "user_id", name = "owner_id")
	private User owner;

	@Builder
	public Store(String name, String address, LocalDateTime openTime, LocalDateTime closeTime, int minimumOrderPrice,
		StoreStatus status,
		User owner) {
		this.name = name;
		this.address = address;
		this.openTime = openTime;
		this.closeTime = closeTime;
		this.minimumOrderPrice = minimumOrderPrice;
		this.status = status;
		this.owner = owner;
	}

	public void close() {
		this.status = StoreStatus.CLOSED;
	}

	public void update(String name, LocalDateTime openTime, LocalDateTime closeTime, int minimumOrderPrice) {
		this.name = name;
		this.openTime = openTime;
		this.closeTime = closeTime;
		this.minimumOrderPrice = minimumOrderPrice;
	}

}
