package com.example.outsourcing_11.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

	//404
	NOT_FOUND_OWNER_COMMENT(HttpStatus.NOT_FOUND, "사장님 댓글이 존재하지 않습니다."),
	ORDER_ALREADY_DELETED(HttpStatus.NOT_FOUND, "요청하신 주문은 존재하지 않거나 삭제되었습니다."),

	//403
	FORBIDDEN_PERMISSION(HttpStatus.FORBIDDEN, "권한이 필요한 요청입니다."),
	FORBIDDEN_USER(HttpStatus.FORBIDDEN, "유효하지 않은 유저입니다."),

	//401
	ORDER_NOT_COMPLETED(HttpStatus.BAD_REQUEST, "리뷰는 배달 완료 이후에만 작성할 수 있습니다."),
	DUPLICATE_COMMENT(HttpStatus.BAD_REQUEST, "이미 댓글을 작성했습니다.");

	private final HttpStatus httpStatus;
	private final String message;

}
