package com.example.outsourcing_11.domain.user.dto;

import jakarta.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PasswordRequestDto {
	
	@NotBlank
	private String password;
}
