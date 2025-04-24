package com.example.outsourcing_11.domain.menu.controller;

import com.example.outsourcing_11.domain.menu.dto.response.MenuUserResponseDto;
import com.example.outsourcing_11.domain.menu.entity.Menu;
import com.example.outsourcing_11.domain.menu.enums.Category;
import com.example.outsourcing_11.domain.menu.service.MenuUserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/menus")
@RequiredArgsConstructor
public class MenuUserController {

    private final MenuUserServiceImpl menuUserServiceImpl;

    @GetMapping
    public ResponseEntity<Slice<MenuUserResponseDto>> findCursorMenuBySize(
        @RequestParam(required = false) Category categoryCursor,
        @RequestParam(required = false) Long lastId,
        @RequestParam(defaultValue = "20") int size) {

        Slice<Menu> menuList = menuUserServiceImpl.findCursorMenuBySize(categoryCursor, lastId, size);
        return new ResponseEntity<>(menuList.map(MenuUserResponseDto::new), HttpStatus.OK);

    }


}
