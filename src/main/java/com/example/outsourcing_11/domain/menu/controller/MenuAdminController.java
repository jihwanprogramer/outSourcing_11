package com.example.outsourcing_11.domain.menu.controller;

import com.example.outsourcing_11.domain.menu.dto.request.MenuSaveRequestDto;
import com.example.outsourcing_11.domain.menu.dto.request.MenuUpdateRequestDto;
import com.example.outsourcing_11.domain.menu.dto.response.MenuAdminResponseDto;
import com.example.outsourcing_11.domain.menu.service.MenuAdminService;
import com.example.outsourcing_11.domain.menu.service.MenuAdminServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/{storeId}/menus")
@RequiredArgsConstructor
public class MenuAdminController {

    private final MenuAdminServiceImpl menuAdminServiceImpl;
    private final MenuAdminService menuAdminService;

    @PreAuthorize("hasRole('사장님')")
    @PostMapping
    public ResponseEntity<MenuAdminResponseDto> createMenu(
        @PathVariable Long storeId,
        @RequestBody MenuSaveRequestDto menuSaveRequestDto,
        HttpServletRequest request) {
        MenuAdminResponseDto saveMenu = menuAdminServiceImpl.createMenu(storeId, menuSaveRequestDto, request);

        return new ResponseEntity<>(saveMenu, HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('사장님')")
    @PatchMapping("/{menuId}")
    public ResponseEntity<MenuAdminResponseDto> updateMenu(
        @PathVariable Long storeId,
        @PathVariable Long menuId,
        @RequestBody MenuUpdateRequestDto dto,
        HttpServletRequest request
    ) {
        MenuAdminResponseDto updateMenu = menuAdminServiceImpl.updateMenu(storeId, menuId, dto, request);
        return new ResponseEntity<>(updateMenu, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('사장님')")
    @DeleteMapping("/{menuId}")
    public ResponseEntity<Void> deleteMenu(
        @PathVariable Long storeId,
        @PathVariable Long menuId,
        HttpServletRequest request) {
        menuAdminService.delete(storeId, menuId, request);

        return new ResponseEntity<>(HttpStatus.OK);
    }

}
