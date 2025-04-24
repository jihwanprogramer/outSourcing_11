package com.example.outsourcing_11.domain.menu.dto.request;

import com.example.outsourcing_11.domain.menu.enums.Category;
import com.example.outsourcing_11.domain.menu.enums.MenuStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class MenuUpdateRequestDto {

    private Category category;

    private String menuName;

    private String content;

    private BigDecimal price;

    private MenuStatus status;

}
