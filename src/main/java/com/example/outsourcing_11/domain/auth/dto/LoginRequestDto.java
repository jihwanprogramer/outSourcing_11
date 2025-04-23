package com.example.outsourcing_11.domain.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class LoginRequestDto {

	@NotBlank
	@Email
	private final String email;

	@NotBlank
	private final String password;
}
