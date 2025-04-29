package com.example.outsourcing_11.domain.user.dto;

import jakarta.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateUserPasswordRequestDto {
	@NotBlank(message = "이전 비밀번호 란은 비어 있을 수 없습니다.")
	private String oldPassword;
	@NotBlank(message = "새로운 비밀번호 란은 비어 있을 수 없습니다.")
	private String newPassword;
}
