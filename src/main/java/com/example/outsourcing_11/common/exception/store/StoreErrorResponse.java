package com.example.outsourcing_11.common.exception.store;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class StoreErrorResponse {

	private final String code;
	private final String message;
	private final int status;

}
