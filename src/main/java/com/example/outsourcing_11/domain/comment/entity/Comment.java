package com.example.outsourcing_11.domain.comment.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
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
import com.example.outsourcing_11.domain.comment.dto.user.RequestCommentDto;
import com.example.outsourcing_11.domain.order.entity.Order;
import com.example.outsourcing_11.domain.store.entity.Store;
import com.example.outsourcing_11.domain.user.entity.User;

@Getter
@Entity
@Table(name = "comments")
public class Comment extends Base {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	private Comment parent;

	@OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Comment> childrenComment = new ArrayList<>();

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "order_id")
	private Order order;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "store_id")
	private Store store;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	private String content;

	private String imageUrl;

	private int rating;

	private boolean isDeleted;

	public Comment() {

	}

	public Comment(RequestCommentDto dto) {

		this.content = dto.getContent();
		this.imageUrl = dto.getImageUrl();
		this.rating = dto.getRating();
	}

	public Comment(String content, String imageUrl, int rating) {

		this.content = content;
		this.imageUrl = imageUrl;
		this.rating = rating;
	}

	public void updateDeleteStatus(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public void updateUserAndStore(User user, Store store) {
		this.user = user;
		this.store = store;
	}
}
