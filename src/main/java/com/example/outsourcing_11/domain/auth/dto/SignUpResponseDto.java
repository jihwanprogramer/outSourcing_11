package com.example.outsourcing_11.domain.auth.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class SignUpResponseDto {

	@NotBlank
	private final String userName;

	@NotBlank
	@Email
	private final String email;

	@NotNull
	private final String phone;

	@NotNull
	private final String address;

	@NotNull
	private final String roel;

	@NotNull
	private final LocalDateTime createAt;
}
