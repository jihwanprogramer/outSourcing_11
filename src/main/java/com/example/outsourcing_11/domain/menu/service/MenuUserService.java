package com.example.outsourcing_11.domain.menu.service;

import com.example.outsourcing_11.domain.menu.dto.response.MenuUserResponseDto;
import com.example.outsourcing_11.domain.menu.enums.Category;
import org.springframework.data.domain.Slice;

public interface MenuUserService {

    Slice<MenuUserResponseDto> findCursorMenuBySize(Long storeId, Category categoryCursor, Long lastId, int size);


}
