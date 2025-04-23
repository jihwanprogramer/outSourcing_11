package com.example.outsourcing_11.domain.store.entity;

import java.time.LocalDateTime;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import com.example.outsourcing_11.domain.Base;
import com.example.outsourcing_11.domain.user.entity.User;

public class Store extends Base {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;
	private LocalDateTime openTime;
	private LocalDateTime closeTime;
	private int minimumOrderPrice;

	@Enumerated(EnumType.STRING)
	private StoreStatus status;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	public Store(String name, LocalDateTime openTime, LocalDateTime closeTime, int minimumOrderPrice,
		StoreStatus status,
		User user) {
		this.name = name;
		this.openTime = openTime;
		this.closeTime = closeTime;
		this.minimumOrderPrice = minimumOrderPrice;
		this.status = status;
		this.user = user;
	}

	public void close() {
		this.status = StoreStatus.CLOSED;
	}

}
