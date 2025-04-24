package com.example.outsourcing_11.common.exception.store;

import lombok.Getter;

@Getter
public class StoreCustomException extends RuntimeException {

	private final StoreErrorCode errorCode;

	public StoreCustomException(StoreErrorCode errorCode) {
		super(errorCode.getMessage());
		this.errorCode = errorCode;
	}

}
