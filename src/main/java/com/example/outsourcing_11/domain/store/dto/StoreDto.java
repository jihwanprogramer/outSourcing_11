package com.example.outsourcing_11.domain.store.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

// 사장님 유저 조회할때 간단하게 가게정보 이름만 같이 출력할때 쓰는 dto

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StoreDto {
	private Long id;
	private String name;
}
