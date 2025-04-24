package com.example.outsourcing_11.common.exception.user;

public class InvalidLoginException extends RuntimeException {
	public InvalidLoginException(String message) {
		super(message);
	}
}
