package com.example.outsourcing_11.domain.menu.service;

import com.example.outsourcing_11.common.Status;
import com.example.outsourcing_11.common.exception.CustomException;
import com.example.outsourcing_11.common.exception.ErrorCode;
import com.example.outsourcing_11.domain.menu.dto.request.MenuSaveRequestDto;
import com.example.outsourcing_11.domain.menu.dto.request.MenuUpdateRequestDto;
import com.example.outsourcing_11.domain.menu.dto.response.MenuAdminResponseDto;
import com.example.outsourcing_11.domain.menu.entity.Menu;
import com.example.outsourcing_11.domain.menu.repository.MenuRepository;
import com.example.outsourcing_11.domain.store.entity.Store;
import com.example.outsourcing_11.domain.store.repository.StoreRepository;
import com.example.outsourcing_11.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class MenuAdminServiceImpl implements MenuAdminService {

    private final MenuRepository menuRepository;
    private final StoreRepository storeRepository;
    private final JwtUtil jwtUtil;

    @Transactional
    @Override
    public MenuAdminResponseDto createMenu(Long storeId, MenuSaveRequestDto dto, HttpServletRequest request) {
        String token = jwtUtil.extractTokenFromRequest(request);
        Long currentUserId = jwtUtil.extractUserId(token);

        Store findStore = storeRepository.findByIdAndOwnerIdAndDeletedFalse(storeId, currentUserId).
            orElseThrow(() -> new CustomException(ErrorCode.ONLY_MY_STORE));

        Menu menu = Menu.of(dto.getCategory(), dto.getMenuName(), dto.getContent(), dto.getPrice(), dto.getStatus(), findStore);
        menuRepository.save(menu);

        return MenuAdminResponseDto.builder()
            .menuId(menu.getId())
            .storeId(findStore.getId())
            .category(menu.getCategory())
            .menuName(menu.getName())
            .content(menu.getContent())
            .menuStatus(menu.getStatus())
            .createdAt(menu.getCreatedAt())
            .build();
    }

    @Transactional
    @Override
    public MenuAdminResponseDto updateMenu(Long storeId, Long menuId, MenuUpdateRequestDto dto, HttpServletRequest request) {

        String token = jwtUtil.extractTokenFromRequest(request);
        Long currentUserId = jwtUtil.extractUserId(token);

        Store findStore = storeRepository.findByIdAndOwnerIdAndDeletedFalse(storeId, currentUserId)
            .orElseThrow(() -> new CustomException(ErrorCode.ONLY_MY_STORE));

        Menu menu = menuRepository.finByIdOrElseThrow(menuId);

        if (findStore.getId().equals(menu.getStore().getId())) {
            menu.update(dto);
        } else {
            throw new CustomException(ErrorCode.MENU_NOT_FOUND);
        }

        return MenuAdminResponseDto.builder()
            .menuId(menu.getId())
            .storeId(findStore.getId())
            .category(menu.getCategory())
            .menuName(menu.getName())
            .content(menu.getContent())
            .menuStatus(menu.getStatus())
            .createdAt(menu.getCreatedAt())
            .build();
    }

    @Transactional
    @Override
    public void delete(Long storeId, Long menuId, HttpServletRequest request) {

        String token = jwtUtil.extractTokenFromRequest(request);
        Long currentUserId = jwtUtil.extractUserId(token);

        Store findStore = storeRepository.findByIdAndOwnerIdAndDeletedFalse(storeId, currentUserId)
            .orElseThrow(() -> new CustomException(ErrorCode.STORE_NOT_FOUND));

        Menu menu = menuRepository.findById(menuId)
            .orElseThrow(() -> new CustomException(ErrorCode.MENU_NOT_FOUND));

        if (!findStore.getId().equals(menu.getStore().getId())) {
            throw new CustomException(ErrorCode.MENU_NOT_FOUND);
        }

        // 메뉴 삭제 처리
        menu.updateDeleteStatus(Status.NON_EXIST.getValue());
        menu.timeWhenDeleted();
        menuRepository.save(menu);

    }
}
