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

import com.example.outsourcing_11.domain.Base;

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

	public void updateDeleteStatus(int isDeleted) {
		this.isDeleted = isDeleted;
	}

}
