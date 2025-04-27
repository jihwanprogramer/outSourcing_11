package com.example.outsourcing_11.common;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import com.example.outsourcing_11.common.exception.menu.MenuNotFoundException;
import com.example.outsourcing_11.common.exception.store.StoreCustomException;
import com.example.outsourcing_11.common.exception.store.StoreErrorCode;
import com.example.outsourcing_11.common.exception.store.StoreErrorResponse;
import com.example.outsourcing_11.common.exception.user.DuplicateUserException;
import com.example.outsourcing_11.common.exception.user.InvalidLoginException;
import com.example.outsourcing_11.common.exception.user.InvalidUserInputException;
import com.example.outsourcing_11.common.exception.user.UnauthorizedAccessException;
import com.example.outsourcing_11.common.exception.user.UnauthorizedException;
import com.example.outsourcing_11.common.exception.user.UserNotFoundException;
import com.example.outsourcing_11.config.security.CustomUserDetails;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<String> handleAccessDenied(AccessDeniedException e) {
		// 현재 로그인된 유저 권한을 확인
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		if (auth != null && auth.getPrincipal() instanceof CustomUserDetails userDetails) {
			String role = userDetails.getUser().getRole().getRoleName();

			// 권한에 따라 메시지 다르게 처리
			if ("고객".equals(role)) {
				return ResponseEntity.status(HttpStatus.FORBIDDEN)
					.body("고객은 해당 기능에 접근할 수 없습니다.");
			} else if ("사장님".equals(role)) {
				return ResponseEntity.status(HttpStatus.FORBIDDEN)
					.body("사장님은 이 기능에 접근할 수 없습니다.");
			}
		}

		// 그 외의 경우 (권한 없음, 비인증 등)
		return ResponseEntity.status(HttpStatus.FORBIDDEN)
			.body("접근 권한이 없습니다.");
	}

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

	// 사용자 입력 오류 400
	@ExceptionHandler(InvalidUserInputException.class)
	public ResponseEntity<String> handleInvalidUserInput(InvalidUserInputException ex) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
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

	// // 본인 계정에만 접근 가능 403
	// @ExceptionHandler(UnauthorizedAccessException.class)
	// public ResponseEntity<String> handleUnauthorizedAccess(UnauthorizedAccessException ex) {
	// 	return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ex.getMessage());
	// }

	@ExceptionHandler(MenuNotFoundException.class)
	public ResponseEntity<String> handleInvalidLogin(MenuNotFoundException ex) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
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

