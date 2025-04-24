package com.example.outsourcing_11.domain.order.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class CancelResponseDto {

    private String message;
    private LocalDateTime canceledAt;

}
