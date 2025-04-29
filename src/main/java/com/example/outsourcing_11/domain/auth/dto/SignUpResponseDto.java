package com.example.outsourcing_11.domain.auth.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SignUpResponseDto {

	private String userName;
	private String email;
	private String phone;
	private String address;
	private String role;
	private LocalDateTime createAt;

	public SignUpResponseDto(String userName, String email) {
		this.userName = userName;
		this.email = email;
	}
}
