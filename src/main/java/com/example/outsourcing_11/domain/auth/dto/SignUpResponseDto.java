package com.example.outsourcing_11.domain.auth.dto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class SignUpResponseDto {

	private final String userName;
	private final String email;
	private final String phone;
	private final String address;
	private final String role;
	private final LocalDateTime createAt;
}
