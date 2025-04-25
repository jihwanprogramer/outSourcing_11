package com.example.outsourcing_11.domain.comment.dto.Owner;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OwnerRequestCommentDto {

	@NotBlank
	@Size(min = 10, message = "10글자 이상 입력해주세요")
	private String content;
}
