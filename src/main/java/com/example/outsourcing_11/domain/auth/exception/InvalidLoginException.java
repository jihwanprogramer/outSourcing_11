package com.example.outsourcing_11.domain.auth.exception;

public class InvalidLoginException extends RuntimeException {
	public InvalidLoginException(String message) {
		super(message);
	}
}
