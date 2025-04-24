package com.example.outsourcing_11.domain.user.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import com.example.outsourcing_11.domain.store.dto.StoreDto;

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
	private List<StoreDto> storeList; //사장님일 경우 가지고있는 가게도 같이 조회

	public UserResponseDto(String userName, String email, String phone, String address, String role) {
		this.userName = userName;
		this.email = email;
		this.phone = phone;
		this.address = address;
		this.role = role;
	}
}
