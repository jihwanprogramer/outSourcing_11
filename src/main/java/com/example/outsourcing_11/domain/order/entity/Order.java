package com.example.outsourcing_11.domain.order.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
import jakarta.persistence.Table;

import lombok.Getter;

import com.example.outsourcing_11.common.Base;
import com.example.outsourcing_11.domain.store.entity.Store;
import com.example.outsourcing_11.domain.user.entity.User;

@Entity
@Getter
@Table(name = "orders")
public class Order extends Base {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "order_id")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@Column(name = "order_date", nullable = false)
	private LocalDateTime orderDate;

	@Enumerated(EnumType.STRING)
	@Column(name = "status", nullable = false)
	private OrderStatus status = OrderStatus.PENDING;

	@Column(name = "total_price", nullable = false)
	private int totalPrice;

	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<OrderItem> items = new ArrayList<>();

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "store_id", nullable = false)
	private Store store;

	public Order(User user, LocalDateTime orderDate, OrderStatus status, int totalPrice, List<OrderItem> items) {
		this.user = user;
		this.orderDate = orderDate;
		this.status = status;
		this.totalPrice = totalPrice;
		this.items = items;

		// 양방향 연관관계 설정
		for (OrderItem item : items) {
			item.setOrder(this);
		}
	}

	public Order() {

	}
	public Order(User user, Store store, LocalDateTime orderDate, OrderStatus status, int totalPrice, List<OrderItem> items) {
		this.user = user;
		this.store = store; // ✅ store 추가
		this.orderDate = orderDate;
		this.status = status;
		this.totalPrice = totalPrice;
		this.items = items;

		// 양방향 연관관계 설정
		for (OrderItem item : items) {
			item.setOrder(this);
		}
	}
	public void changeStatus(OrderStatus newStatus) {
		this.status = newStatus;
	}
}
