package com.example.outsourcing_11.common.exception.comment;

import org.springframework.http.HttpStatus;
import com.example.outsourcing_11.common.exception.CustomException;

public class IllegalStateException extends CustomException {
	public IllegalStateException(String message) {
		super(message, HttpStatus.CONFLICT);
	}
}
