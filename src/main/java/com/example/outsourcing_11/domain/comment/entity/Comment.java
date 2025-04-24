package com.example.outsourcing_11.domain.comment.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import lombok.Getter;

import com.example.outsourcing_11.common.Base;
import com.example.outsourcing_11.domain.comment.dto.RequestCommentDto;
import com.example.outsourcing_11.domain.user.entity.User;

@Getter
@Entity
@Table(name = "comments")
public class Comment extends Base {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	private Comment parent;

	@OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Comment> childrenComment = new ArrayList<>();

	// @ManyToOne(fetch = FetchType.LAZY)
	// private Order order;
	//
	// @ManyToOne(fetch = FetchType.LAZY)
	// private User user;

	private String content;

	private String imageUrl;

	private int rating;

	private int isDeleted;

	public Comment() {

	}

	public Comment(RequestCommentDto dto) {

		this.content = dto.getContent();
		this.imageUrl = dto.getImageUrl();
		this.rating = dto.getRating();
	}

	public void updateDeleteStatus(int isDeleted) {
		this.isDeleted = isDeleted;
	}

}
