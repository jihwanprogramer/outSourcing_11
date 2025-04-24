package com.example.outsourcing_11.domain.menu.service;

import com.example.outsourcing_11.domain.menu.dto.request.MenuSaveRequestDto;
import com.example.outsourcing_11.domain.menu.dto.request.MenuUpdateRequestDto;
import com.example.outsourcing_11.domain.menu.dto.response.MenuAdminResponseDto;

public interface MenuAdminService {

    MenuAdminResponseDto createMenu(Long storeId, MenuSaveRequestDto menuSaveRequestDto);

    MenuAdminResponseDto updateMenu(Long storeId, Long menuId, MenuUpdateRequestDto dto);

    void delete(Long storeId, Long menuId);
}
