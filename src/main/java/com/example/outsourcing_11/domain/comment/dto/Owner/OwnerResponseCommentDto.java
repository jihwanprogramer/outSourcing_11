package com.example.outsourcing_11.domain.comment.dto.Owner;

import lombok.Getter;

import com.example.outsourcing_11.domain.comment.entity.OwnerComment;

@Getter
public class OwnerResponseCommentDto {

	private String content;

	public OwnerResponseCommentDto(OwnerComment ownerComment) {
		this.content = ownerComment.getContent();
	}

}
