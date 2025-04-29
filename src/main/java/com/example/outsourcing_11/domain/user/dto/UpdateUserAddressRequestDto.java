package com.example.outsourcing_11.domain.user.dto;

import jakarta.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateUserAddressRequestDto {
	@NotBlank(message = "주소는 비어 있을 수 없습니다.")
	private String newAddress;
}
