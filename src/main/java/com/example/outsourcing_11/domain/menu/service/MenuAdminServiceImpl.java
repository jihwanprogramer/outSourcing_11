package com.example.outsourcing_11.domain.menu.service;

import com.example.outsourcing_11.domain.menu.dto.request.MenuSaveRequestDto;
import com.example.outsourcing_11.domain.menu.dto.request.MenuUpdateRequestDto;
import com.example.outsourcing_11.domain.menu.dto.response.MenuAdminResponseDto;
import com.example.outsourcing_11.domain.menu.entity.Menu;
import com.example.outsourcing_11.domain.menu.repository.MenuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MenuAdminServiceImpl implements MenuAdminService {

    private final MenuRepository menuRepository;

    @Override
    @Transactional
    public MenuAdminResponseDto createMenu(MenuSaveRequestDto dto) {
        Menu menu = Menu.of(dto.getCategory(), dto.getMenuName(), dto.getContent(), dto.getPrice(), dto.getStatus());
        menuRepository.save(menu);

        return new MenuAdminResponseDto(menu);
    }

    @Override
    @Transactional
    public MenuAdminResponseDto updateMenu(Long menuId, MenuUpdateRequestDto dto) {
        Menu menu = menuRepository.finByIdOrElseThrow(menuId);
        menu.update(dto);

        return new MenuAdminResponseDto(menu);
    }

    @Override
    @Transactional
    public void delete(Long menuId) {
        Menu menu = menuRepository.finByIdOrElseThrow(menuId);
        menu.timeWhenDeleted();
        menuRepository.save(menu);
    }
}
