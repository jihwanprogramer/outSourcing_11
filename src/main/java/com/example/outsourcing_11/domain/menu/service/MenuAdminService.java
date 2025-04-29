package com.example.outsourcing_11.domain.menu.service;

import com.example.outsourcing_11.domain.menu.dto.request.MenuSaveRequestDto;
import com.example.outsourcing_11.domain.menu.dto.request.MenuUpdateRequestDto;
import com.example.outsourcing_11.domain.menu.dto.response.MenuAdminResponseDto;
import jakarta.servlet.http.HttpServletRequest;

public interface MenuAdminService {


    MenuAdminResponseDto createMenu(Long storeId, MenuSaveRequestDto dto, HttpServletRequest request);

    MenuAdminResponseDto updateMenu(Long storeId, Long menuId, MenuUpdateRequestDto dto, HttpServletRequest request);

    void delete(Long storeId, Long menuId, HttpServletRequest request);
}
