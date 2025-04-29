package com.example.outsourcing_11.domain.ownercomment.dto;

import lombok.Getter;

import com.example.outsourcing_11.domain.ownercomment.entity.OwnerComment;

@Getter
public class OwnerResponseCommentDto {

	private final Long id;
	private final String content;
	private final String createdAt;
	private final String modifiedAt;

	public OwnerResponseCommentDto(OwnerComment ownerComment) {
		this.id = ownerComment.getId();
		this.content = ownerComment.getContent();
		this.createdAt = ownerComment.getCreatedAt().toString();
		this.modifiedAt = ownerComment.getUpdatedAt().toString();
	}
}
