package com.example.outsourcing_11.domain.comment.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import lombok.Getter;

import com.example.outsourcing_11.common.Base;
import com.example.outsourcing_11.domain.comment.dto.Owner.OwnerRequestCommentDto;

@Getter
@Entity
@Table(name = "OwnerComments")
public class OwnerComment extends Base {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToOne
	@JoinColumn(name = "comment_id", nullable = false, unique = true)
	private Comment comment;

	private String content;

	private boolean isDeleted;

	public void updateDeleteStatus(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public OwnerComment() {

	}

	public OwnerComment(OwnerRequestCommentDto dto) {
		this.content = dto.getContent();

	}

	public void updateContent(String content) {
		this.content = content;
	}

}
