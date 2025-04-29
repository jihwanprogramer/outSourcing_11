package com.example.outsourcing_11.domain.user.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeleteAndUpdateUserResponseDto {
	private String message;
	private LocalDateTime deletedAt;
}
