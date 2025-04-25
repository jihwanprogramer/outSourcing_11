package com.example.outsourcing_11.common.exception;

import org.springframework.http.HttpStatus;

public class CustomException extends RuntimeException {
	public CustomException(String message) {
		super(message);
	}

	public CustomException(String message, HttpStatus status) {
		super(message);
	}
}
