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
	DUPLICATE_COMMENT(HttpStatus.BAD_REQUEST, "이미 댓글을 작성했습니다."),

	// Cart 관련 에러 추가
	MENU_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 메뉴입니다."), // 404
	USER_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 사용자입니다."), // 404
	INVALID_QUANTITY(HttpStatus.BAD_REQUEST, "수량은 1 이상이어야 합니다."), // 400
	CART_EMPTY(HttpStatus.NOT_FOUND, "장바구니가 비어 있습니다."), // 404
	CART_ITEM_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 장바구니 항목입니다."), //404

	// 주문 관련 에러 추가
	INVALID_COUPON_OR_POINT(HttpStatus.BAD_REQUEST, "유효하지 않은 쿠폰이거나 사용할 수 없는 포인트입니다."), //400
	ORDER_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 주문입니다."), //404
	INVALID_MENU_IN_ORDER(HttpStatus.BAD_REQUEST, "주문 항목에 유효하지 않은 메뉴가 포함되어 있습니다."), //400
	CANNOT_CHANGE_COMPLETED_ORDER(HttpStatus.BAD_REQUEST, "이미 완료된 주문은 변경할 수 없습니다."), //400
	CANNOT_CANCEL_COMPLETED_OR_CANCELED_ORDER(HttpStatus.BAD_REQUEST, "이미 취소되었거나 완료된 주문은 취소할 수 없습니다."), //400
	AUTHENTICATION_REQUIRED(HttpStatus.UNAUTHORIZED, "로그인 후 이용 가능한 기능입니다."); //401

	private final HttpStatus httpStatus;
	private final String message;

}
