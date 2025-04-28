package com.example.outsourcing_11.common.exception.comment;

import org.springframework.http.HttpStatus;
import com.example.outsourcing_11.common.exception.CustomException;

public class AccessDeniedException extends CustomException {
	public AccessDeniedException(String message) {
		super(message, HttpStatus.FORBIDDEN);
	}
}
