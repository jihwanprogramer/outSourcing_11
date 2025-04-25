package com.example.outsourcing_11.domain.store.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import lombok.Getter;
import lombok.NoArgsConstructor;

import com.example.outsourcing_11.common.Base;

@Getter
@Entity
@NoArgsConstructor
public class Notice extends Base {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "store_id", nullable = false)
	private Store store;

	@Column(nullable = false, columnDefinition = "TEXT")
	private String content;

	public Notice(Store store, String content) {
		this.store = store;
		this.content = content;
	}

	public void update(String content) {
		this.content = content;
	}
}
