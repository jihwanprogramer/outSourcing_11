package com.example.outsourcing_11.common.exception.user;

import lombok.Getter;

@Getter
public class UserCustomException extends RuntimeException {
	private final UserErrorCode errorCode;

	public UserCustomException(UserErrorCode errorCode) {
		super(errorCode.getMessage());
		this.errorCode = errorCode;
	}
}
