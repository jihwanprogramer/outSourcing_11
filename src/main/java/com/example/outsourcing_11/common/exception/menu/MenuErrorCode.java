package com.example.outsourcing_11.common.exception.menu;

import lombok.Getter;

@Getter
public enum MenuErrorCode {
    ONLY_MY_STORE("AUTH-002", "본인 가게에만 접근 가능합니다.", 401),
    STORE_NOT_FOUND("NF-003", "존재하지 않는 가게입니다.", 404),
    MENU_NOT_FOUND("NF-001", "해당 가게에 입력하신 메뉴가 없습니다.", 404);

    private final String code;
    private final String message;
    private final int status;

    MenuErrorCode(String code, String message, int status) {
        this.code = code;
        this.message = message;
        this.status = status;
    }
}
