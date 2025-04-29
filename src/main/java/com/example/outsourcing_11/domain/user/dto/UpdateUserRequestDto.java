package com.example.outsourcing_11.domain.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateUserRequestDto {
	@NotBlank(message = "이름은 비어 있을 수 없습니다.")
	private String userName;
	@Email(message = "이메일 형식이 올바르지 않습니다.")
	private String email;
	@NotBlank(message = "전화번호는 비어 있을 수 없습니다.")
	private String phone;
}
