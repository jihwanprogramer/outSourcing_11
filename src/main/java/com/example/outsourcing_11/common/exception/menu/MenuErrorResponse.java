package com.example.outsourcing_11.common.exception.menu;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MenuErrorResponse {
    private final String code;
    private final String message;
    private final int status;
}
