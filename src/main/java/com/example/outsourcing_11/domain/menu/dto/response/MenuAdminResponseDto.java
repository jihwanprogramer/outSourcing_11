package com.example.outsourcing_11.domain.menu.dto.response;

import com.example.outsourcing_11.domain.menu.entity.Menu;
import com.example.outsourcing_11.domain.menu.enums.Category;
import com.example.outsourcing_11.domain.menu.enums.MenuStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class MenuAdminResponseDto {
    private final Long menuId;
    private final Category category;
    private final String menuName;
    private final String content;
    private final MenuStatus menuStatus;
    private final LocalDateTime createdAt;

    public MenuAdminResponseDto(Menu menu) {
        this.menuId = menu.getId();
        this.category = menu.getCategory();
        this.menuName = menu.getName();
        this.content = menu.getContent();
        this.menuStatus = menu.getStatus();
        this.createdAt = menu.getCreatedAt();
    }

}
