package com.example.outsourcing_11.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    //BADRequest//
    //가게
    LIMIT_THREE("BAD-001", "최대 3개의 가게까지 등록할 수 있습니다.", 400),
    ALREADY_FAVORITE("BAD-002", "이미 즐겨찾기 등록된 가게입니다.", 400),
    NON_CONTENT("BAD-003", "내용을 입력해주세요", 400),
    PERIOD_ERROR("BAD-004", "월간,주간,일간 단위로 조회 가능합니다.", 400),
    NON_FAVORITE("BAD-005", "즐겨찾기 상태가 아닙니다.", 400),

    INVALID_QUANTITY("BAD-006", "수량은 1 이상이어야 합니다.", 400), // 400
    //주문
    INVALID_COUPON_OR_POINT("BAD-007", "유효하지 않은 쿠폰이거나 사용할 수 없는 포인트입니다.", 400), //400
    INVALID_MENU_IN_ORDER("BAD-008", "주문 항목에 유효하지 않은 메뉴가 포함되어 있습니다.", 400), //400
    CANNOT_CHANGE_COMPLETED_ORDER("BAD-009", "이미 완료된 주문은 변경할 수 없습니다.", 400), //400
    CANNOT_CANCEL_COMPLETED_OR_CANCELED_ORDER("BAD-010", "이미 취소되었거나 완료된 주문은 취소할 수 없습니다.", 400), //400
    //리뷰
    ORDER_NOT_COMPLETED("BAD-011", "리뷰는 배달 완료 이후에만 작성할 수 있습니다.", 400),
    DUPLICATE_COMMENT("BAD-012", "이미 댓글을 작성했습니다.", 400),
    //유저
    DUPLICATE_USER("BAD-013", "이미 존재하는 사용자 이메일입니다.", 400),
    SAME_PASSWORD("BAD-014", "현재 비밀번호와 동일합니다.", 400),
    INVALID_PASSWORD("BAD-015", "비밀번호가 일치하지 않습니다.", 401),

    //AUTHRequest//

    //가게
    ONLY_MY_STORE("AUTH-001", "본인 가게에만 접근 가능합니다.", 401),
    //메뉴

    //카트

    //주문

    //리뷰
    FORBIDDEN_PERMISSION("AUTH-002", "권한이 필요한 요청입니다.", 403),
    FORBIDDEN_USER("AUTH-003", "유효하지 않은 유저입니다.", 403),
    //유저
    UNAUTHORIZED_ACCESS("AUTH-004", "탈퇴한 회원 정보입니다.", 403),
    UNAUTHORIZED_COOKIE("AUTH-005", "수정 및 삭제 인증 쿠키가 없습니다.", 401),
    AUTHORIZATION_HEADER_MISSING("AUTH-006", "Authorization 헤더가 비어 있습니다.", 401),

    //NotFound// 
    // 메뉴
    MENU_NOT_FOUND("NF-001", "해당 가게에 입력하신 메뉴가 없습니다.", 404),
    //가게
    KEYWORD_NOT_FOUND("NF-002", "해당 키워드가 존재하지 않습니다.", 404),
    STORE_NOT_FOUND("NF-003", "존재하지 않는 가게입니다.", 404),
    NO_NOTICE("NF-004", "존재하지 않는 공지입니다.", 404),
    MY_STORE_NOT_FOUND("NF-005", "내 소유의 가게가 존재하지 않습니다.", 404),
    NO_STORE_IN_CATEGORY("NF-006", "해당 카테고리에 가게가 없습니다.", 404),

    //카트
    CART_ITEM_NOT_FOUND("NF-007", "존재하지 않는 장바구니 항목입니다.", 404), //404
    CART_EMPTY("NF-008", "장바구니가 비어 있습니다.", 404), // 404
    //주문
    ORDER_NOT_FOUND("NF-009", "존재하지 않는 주문입니다.", 404),

    //리뷰
    NOT_FOUND_OWNER_COMMENT("NF-010", "사장님 댓글이 존재하지 않습니다.", 404),
    ORDER_ALREADY_DELETED("NF-011", "요청하신 주문은 존재하지 않거나 삭제되었습니다.", 404),
    //유저
    USER_NOT_FOUND("NF-013", "사용자를 찾을 수 없습니다.", 404),


    NOT_FOUND_REVIEW("NF-014", "요청하신 리뷰를 찾을 수 없습니다.", 404);
    // 회원 관련 에러코드

    // Cart 관련 에러 추가


    // 주문 관련 에러 추가
    //401

    private final String code;
    private final String message;
    private final int status;
}
