package com.example.outsourcing_11.domain.menu.service;

import com.example.outsourcing_11.domain.menu.dto.request.MenuSaveRequestDto;
import com.example.outsourcing_11.domain.menu.dto.request.MenuUpdateRequestDto;
import com.example.outsourcing_11.domain.menu.dto.response.MenuAdminResponseDto;
import com.example.outsourcing_11.domain.menu.entity.Menu;
import com.example.outsourcing_11.domain.menu.repository.MenuRepository;
import com.example.outsourcing_11.domain.store.entity.Store;
import com.example.outsourcing_11.domain.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MenuAdminServiceImpl implements MenuAdminService {

    private final MenuRepository menuRepository;
    private final StoreRepository storeRepository;

    @Override
    @Transactional
    public MenuAdminResponseDto createMenu(Long storeId, MenuSaveRequestDto dto) {

        Store findStore = storeRepository.finByIdOrElseThrow(storeId);
        Menu menu = Menu.of(dto.getCategory(), dto.getMenuName(), dto.getContent(), dto.getPrice(), dto.getStatus(), findStore);
        menuRepository.save(menu);

        return new MenuAdminResponseDto(menu);
    }

    @Override
    @Transactional
    public MenuAdminResponseDto updateMenu(Long storeId, Long menuId, MenuUpdateRequestDto dto) {
        Store findStore = storeRepository.finByIdOrElseThrow(storeId);
        Menu menu = menuRepository.finByIdOrElseThrow(menuId);

        if (findStore.getId().equals(menu.getStore().getId())) {
            menu.update(dto);
        } else {
            throw new RuntimeException("해당 가게에 입력하신 메뉴가 없습니다");
        }

        return new MenuAdminResponseDto(menu);
    }

    @Override
    @Transactional
    public void delete(Long storeId, Long menuId) {
        Menu menu = menuRepository.finByIdOrElseThrow(menuId);

        if (storeId.equals(menu.getStore().getId())) {
            menu.timeWhenDeleted();
            menuRepository.save(menu);
        } else {
            throw new RuntimeException("해당 가게에 입력하신 메뉴가 없습니다");
        }

    }
}
