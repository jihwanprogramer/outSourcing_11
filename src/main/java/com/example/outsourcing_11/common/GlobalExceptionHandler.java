package com.example.outsourcing_11.common;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import com.example.outsourcing_11.common.exception.store.StoreCustomException;
import com.example.outsourcing_11.common.exception.store.StoreErrorCode;
import com.example.outsourcing_11.common.exception.store.StoreErrorResponse;
import com.example.outsourcing_11.common.exception.user.DuplicateUserException;
import com.example.outsourcing_11.common.exception.user.InvalidLoginException;
import com.example.outsourcing_11.common.exception.user.UnauthorizedAccessException;
import com.example.outsourcing_11.common.exception.user.UnauthorizedException;
import com.example.outsourcing_11.common.exception.user.UserNotFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {

	// 사용자를 찾을 수 없을 때 404(예: ID가 없는 경우)
	@ExceptionHandler(UserNotFoundException.class)
	public ResponseEntity<String> handleUserNotFoundException(UserNotFoundException ex) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
	}

	// @Valid 관련 오류 처리 -> 메세지만 띄움
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, String>> handleValidationException(MethodArgumentNotValidException ex) {
		Map<String, String> errors = new HashMap<>();

		for (FieldError error : ex.getBindingResult().getFieldErrors()) {
			errors.put("message", error.getDefaultMessage()); // 필드명 : 메시지
		}

		return ResponseEntity.badRequest().body(errors); // 400
	}

	// 중복 회원가입 409
	@ExceptionHandler(DuplicateUserException.class)
	public ResponseEntity<String> handleDuplicateUser(DuplicateUserException ex) {
		return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
	}

	// 로그인 실패 401
	@ExceptionHandler(InvalidLoginException.class)
	public ResponseEntity<String> handleInvalidLogin(InvalidLoginException ex) {
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());
	}

	// 유효하지않은 토큰
	@ExceptionHandler(UnauthorizedException.class)
	public ResponseEntity<String> handleInvalidLogin(UnauthorizedException ex) {
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());
	}

	// 비활성계정 접근불가 403
	@ExceptionHandler(UnauthorizedAccessException.class)
	public ResponseEntity<String> handleUnauthorizedAccess(UnauthorizedAccessException ex) {
		return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ex.getMessage());
	}

	// 모든 예외 처리 (예상하지 못한 오류)
	@ExceptionHandler(Exception.class)
	public ResponseEntity<String> handleAllExceptions(Exception ex) {
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류 발생: " + ex.getMessage());
	}

	/**
	 * store custom exception
	 */

	@ExceptionHandler(StoreCustomException.class)
	public ResponseEntity<StoreErrorResponse> handleCustomException(StoreCustomException ex) {
		StoreErrorCode errorCode = ex.getErrorCode();
		StoreErrorResponse response = new StoreErrorResponse(errorCode.getCode(), errorCode.getMessage(),
			errorCode.getStatus());
		return new ResponseEntity<>(response, HttpStatus.valueOf(errorCode.getStatus()));
	}

}

