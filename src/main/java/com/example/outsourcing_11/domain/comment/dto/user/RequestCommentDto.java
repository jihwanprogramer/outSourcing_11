package com.example.outsourcing_11.domain.comment.dto.user;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RequestCommentDto {

	@NotBlank(message = "공백을 입력했습니다.")
	@Size(min = 10, message = "10글자 이상 입력해주세요")
	private String content;

	@Max(value = 5)
	@Min(value = 0)
	private int rating;

	private String imageUrl;
}
