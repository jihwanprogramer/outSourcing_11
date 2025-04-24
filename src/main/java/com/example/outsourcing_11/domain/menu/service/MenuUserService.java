package com.example.outsourcing_11.domain.menu.service;

import com.example.outsourcing_11.domain.menu.entity.Menu;
import com.example.outsourcing_11.domain.menu.enums.Category;
import org.springframework.data.domain.Slice;

public interface MenuUserService {
    Slice<Menu> findCursorMenuBySize(Category categoryCursor, Long lastId, int size);
}
