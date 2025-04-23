package com.example.outsourcing_11.domain.auth.exception;

//중복 유저 예외
public class DuplicateUserException extends RuntimeException {
	public DuplicateUserException(String message) {
		super(message);
	}
}
