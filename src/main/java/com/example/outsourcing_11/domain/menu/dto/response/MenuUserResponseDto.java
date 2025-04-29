package com.example.outsourcing_11.domain.menu.dto.response;

import com.example.outsourcing_11.domain.menu.entity.Menu;
import com.example.outsourcing_11.domain.menu.enums.Category;
import com.example.outsourcing_11.domain.menu.enums.MenuStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class MenuUserResponseDto {
    private final Long menuId;
    private final Category category;
    private final String menuName;
    private final String content;
    private final MenuStatus menuStatus;
    private final LocalDateTime createdAt;
    private final long commentCount;

    public MenuUserResponseDto(Menu menu, long commentCount) {
        this.menuId = menu.getId();
        this.category = menu.getCategory();
        this.menuName = menu.getName();
        this.content = menu.getContent();
        this.menuStatus = menu.getStatus();
        this.createdAt = menu.getCreatedAt();
        this.commentCount = commentCount;
    }

}
