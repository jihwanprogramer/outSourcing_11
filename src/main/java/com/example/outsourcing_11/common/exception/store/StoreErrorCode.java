package com.example.outsourcing_11.common.exception.store;

import lombok.Getter;

@Getter
public enum StoreErrorCode {
	LIMIT_THREE("BAD-001", "최대 3개의 가게까지 등록할 수 있습니다.", 400),
	NON_CATEGORY("BAD-002", "지원하지 않는 카테고리입니다.", 400),
	ALREADY_FAVORITE("BAD-003", "이미 즐겨찾기 등록된 가게입니다.", 400),
	NON_CONTENT("BAD-004", "내용을 입력해주세요", 400),
	PERIOD_ERROR("BAD-005", "월간,주간,일간 단위로 조회 가능합니다.", 400),
	NON_FAVORITE("BAD-006", "즐겨찾기 상태가 아닙니다.", 400),
	ONLY_OWNER("AUTH-001", "사장님만 이용가능한 기능입니다.", 401),
	ONLY_MY_STORE("AUTH-002", "본인 가게에만 접근 가능합니다.", 401),
	LOGIN_PLZ("AUTH-003", "로그인 후 이용가능한 기능입니다.", 401),
	ALREADY_CLOSE("NF-001", "폐업된 가게입니다.", 404),
	KEYWORD_NOT_FOUND("NF-002", "해당 키워드가 존재하지 않습니다.", 404),
	STORE_NOT_FOUND("NF-003", "존재하지 않는 가게입니다.", 404),
	NO_NOTICE("NF-004", "존재하지 않는 공지입니다.", 404),
	USER_NOT_FOUND("NF-005", "사용자를 찾을 수 없습니다.", 404);

	private final String code;
	private final String message;
	private final int status;

	StoreErrorCode(String code, String message, int status) {
		this.code = code;
		this.message = message;
		this.status = status;
	}

}
