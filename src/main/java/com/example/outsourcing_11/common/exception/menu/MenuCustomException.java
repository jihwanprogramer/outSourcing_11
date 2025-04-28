package com.example.outsourcing_11.common.exception.menu;

import lombok.Getter;

@Getter
public class MenuCustomException extends RuntimeException {

    private final MenuErrorCode errorCode;

    public MenuCustomException(MenuErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
