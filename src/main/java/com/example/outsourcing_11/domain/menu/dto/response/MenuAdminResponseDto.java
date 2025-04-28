package com.example.outsourcing_11.domain.menu.dto.response;

import com.example.outsourcing_11.domain.menu.enums.Category;
import com.example.outsourcing_11.domain.menu.enums.MenuStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
public class MenuAdminResponseDto {
    private final Long menuId;
    private final Long storeId;
    private final Category category;
    private final String menuName;
    private final String content;
    private final MenuStatus menuStatus;
    private final LocalDateTime createdAt;

}
