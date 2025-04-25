package com.example.outsourcing_11.domain.user.dto;

import java.util.List;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import com.example.outsourcing_11.common.UserRole;
import com.example.outsourcing_11.domain.store.dto.StoreDto;
import com.example.outsourcing_11.domain.user.entity.User;
import com.fasterxml.jackson.annotation.JsonInclude;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
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

	public UserResponseDto(User user) {
		this.userName = user.getName();
		this.email = user.getEmail();
		this.phone = user.getPhone();
		this.address = user.getAddress();
		this.role = user.getRole().getRoleName();

		// 사장님일 경우에만 storeList 포함
		if (user.getRole() == UserRole.OWNER && user.getStoreList() != null) {
			this.storeList = user.getStoreList().stream()
				.map(store -> new StoreDto(store.getId(), store.getName()))
				.collect(Collectors.toList());
		} else {
			this.storeList = null;
		}
	}

}
