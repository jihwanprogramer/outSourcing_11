package com.example.outsourcing_11.domain.user.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UserResponseDto {
	private final String userName;
	private final String email;
	private final String phone;
	private final String address;
	private final String role;
	// private final Store store; 사장님일 경우 가지고있는 가게도 같이 조회예정
}
