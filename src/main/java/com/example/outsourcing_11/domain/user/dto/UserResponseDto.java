package com.example.outsourcing_11.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponseDto {
	private String userName;
	private String email;
	private String phone;
	private String address;
	private String role;
	// private final Store store; 사장님일 경우 가지고있는 가게도 같이 조회예정
}
