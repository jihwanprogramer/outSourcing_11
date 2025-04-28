package com.example.outsourcing_11.common.exception.comment;

import org.springframework.http.HttpStatus;
import com.example.outsourcing_11.common.exception.CustomException;

public class InvalidRequestException extends CustomException {
	public InvalidRequestException(String message) {
		super(message, HttpStatus.BAD_REQUEST);
	}
}
