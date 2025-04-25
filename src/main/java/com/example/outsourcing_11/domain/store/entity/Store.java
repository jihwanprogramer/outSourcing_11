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

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import com.example.outsourcing_11.common.Base;
import com.example.outsourcing_11.common.Status;
import com.example.outsourcing_11.domain.menu.entity.Menu;
import com.example.outsourcing_11.domain.store.dto.StoreRequestDto;
import com.example.outsourcing_11.domain.user.entity.User;

@Builder
@AllArgsConstructor
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

	@Column(columnDefinition = "TINYINT(1) DEFAULT 1")
	private boolean deleted = Status.EXIST.getValue();

	@Builder
	public Store(StoreRequestDto dto, User owner) {
		this.name = name;
		this.address = address;
		this.openTime = openTime;
		this.closeTime = closeTime;
		this.minimumOrderPrice = minimumOrderPrice;
		this.status = status;
		this.category = category;
		this.owner = owner;
		this.deleted = isDeleted();
	}

	public void close() {
		this.status = StoreStatus.CLOSED;
	}

	public void update(StoreRequestDto requestDto) {
		this.name = name;
		this.openTime = openTime;
		this.closeTime = closeTime;
		this.minimumOrderPrice = minimumOrderPrice;
	}

	public void softDelete() {
		this.deletedAt = LocalDateTime.now();
		this.deleted = Status.NON_EXIST.getValue();
	}

}
