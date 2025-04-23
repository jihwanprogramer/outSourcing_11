package com.example.outsourcing_11.global;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import com.example.outsourcing_11.domain.auth.exception.DuplicateUserException;

@RestControllerAdvice
public class GlobalExceptionHandler {

	// // 사용자를 찾을 수 없을 때 (예: ID가 없는 경우)
	// @ExceptionHandler(UserNotFoundException.class)
	// public ResponseEntity<String> handleUserNotFoundException(UserNotFoundException ex) {
	// 	return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
	// }

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

	// // 로그인 실패 401
	// @ExceptionHandler(InvalidLoginException.class)
	// public ResponseEntity<String> handleInvalidLogin(InvalidLoginException ex) {
	// 	return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());
	// }
	//
	// // 사용자 입력 오류 400
	// @ExceptionHandler(InvalidUserInputException.class)
	// public ResponseEntity<String> handleInvalidUserInput(InvalidUserInputException ex) {
	// 	return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
	// }
	//
	// // 본인 계정에만 접근 가능 403
	// @ExceptionHandler(UnauthorizedAccessException.class)
	// public ResponseEntity<String> handleUnauthorizedAccess(UnauthorizedAccessException ex) {
	// 	return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ex.getMessage());
	// }

	// 모든 예외 처리 (예상하지 못한 오류)
	@ExceptionHandler(Exception.class)
	public ResponseEntity<String> handleAllExceptions(Exception ex) {
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류 발생: " + ex.getMessage());
	}

}

