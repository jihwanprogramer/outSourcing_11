package com.example.outsourcing_11.domain.ownercomment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OwnerRequestCommentDto {

	@NotBlank
	@Size(min = 10, message = "10글자 이상 입력해주세요")
	private String content;
}
