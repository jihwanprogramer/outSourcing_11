package com.example.outsourcing_11.domain.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class SignUpRequestDto {

	@NotBlank
	private final String userName;

	@NotBlank
	@Email
	private final String email;

	@NotBlank
	@Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\",.<>/?]).{8,}$",
		message = "비밀번호는 영문, 숫자, 특수문자를 포함한 8자 이상이어야 합니다") // 실패시 400
	private final String password;

	@NotNull
	private final String phone;

	@NotNull
	private final String address;

	@NotBlank
	private final String role;
}
