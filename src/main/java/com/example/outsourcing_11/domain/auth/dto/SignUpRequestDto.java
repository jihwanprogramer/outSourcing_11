package com.example.outsourcing_11.domain.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SignUpRequestDto {

	@NotBlank
	private String userName;

	@NotBlank
	@Email
	private String email;

	@NotBlank
	@Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\",.<>/?]).{8,}$",
		message = "비밀번호는 영문, 숫자, 특수문자를 포함한 8자 이상이어야 합니다") // 실패시 400
	private String password;

	@NotNull
	private String phone;

	@NotNull
	private String address;

	@NotBlank

	private String role;

}
