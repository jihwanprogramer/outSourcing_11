package com.example.outsourcing_11.common.exception.comment;

import org.springframework.http.HttpStatus;
import com.example.outsourcing_11.common.exception.CustomException;

public class EntityNotFoundException extends CustomException {
	public EntityNotFoundException(String message) {
		super(message, HttpStatus.NOT_FOUND);
	}
}
