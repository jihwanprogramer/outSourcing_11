package com.example.outsourcing_11.domain.menu.controller;

import com.example.outsourcing_11.domain.menu.dto.request.MenuSaveRequestDto;
import com.example.outsourcing_11.domain.menu.dto.request.MenuUpdateRequestDto;
import com.example.outsourcing_11.domain.menu.dto.response.MenuAdminResponseDto;
import com.example.outsourcing_11.domain.menu.service.MenuAdminService;
import com.example.outsourcing_11.domain.menu.service.MenuAdminServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/{storeId}/menus")
@RequiredArgsConstructor
public class MenuAdminController {

    private final MenuAdminServiceImpl menuAdminServiceImpl;
    private final MenuAdminService menuAdminService;

    @PostMapping
    public ResponseEntity<MenuAdminResponseDto> createMenu(
        @PathVariable Long storeId,
        @RequestBody MenuSaveRequestDto menuSaveRequestDto) {
        MenuAdminResponseDto saveMenu = menuAdminServiceImpl.createMenu(storeId, menuSaveRequestDto);

        return new ResponseEntity<>(saveMenu, HttpStatus.CREATED);
    }

    @PatchMapping("/{menuId}")
    public ResponseEntity<MenuAdminResponseDto> updateMenu(
        @PathVariable Long storeId,
        @PathVariable Long menuId,
        @RequestBody MenuUpdateRequestDto dto
    ) {
        MenuAdminResponseDto updateMenu = menuAdminServiceImpl.updateMenu(storeId, menuId, dto);
        return new ResponseEntity<>(updateMenu, HttpStatus.OK);
    }

    @DeleteMapping("/{menuId}")
    public ResponseEntity<Void> deleteMenu(@PathVariable Long storeId, @PathVariable Long menuId) {
        menuAdminService.delete(storeId, menuId);

        return new ResponseEntity<>(HttpStatus.OK);
    }

}
