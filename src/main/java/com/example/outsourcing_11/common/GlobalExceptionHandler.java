package com.example.outsourcing_11.common;

import com.example.outsourcing_11.common.exception.CustomException;
import com.example.outsourcing_11.common.exception.ErrorCode;
import com.example.outsourcing_11.common.exception.ErrorResponse;
import com.example.outsourcing_11.config.security.CustomUserDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

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

    // 모든 예외 처리 (예상하지 못한 오류)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleAllExceptions(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류 발생: " + ex.getMessage());
    }

    /**
     * custom exception
     */

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(CustomException ex) {
        ErrorCode errorCode = ex.getErrorCode();
        ErrorResponse response = new ErrorResponse(errorCode.getCode(), errorCode.getMessage(),
            errorCode.getStatus());
        return new ResponseEntity<>(response, HttpStatus.valueOf(errorCode.getStatus()));
    }

}

