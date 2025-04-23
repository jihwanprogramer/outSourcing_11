package com.example.outsourcing_11.domain.comment.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RequestCommentDto {

	private String content;
	private int rating;
	private String imageUrl;
}
