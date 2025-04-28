package com.example.outsourcing_11.domain.menu.controller;

import com.example.outsourcing_11.domain.menu.dto.request.MenuSaveRequestDto;
import com.example.outsourcing_11.domain.menu.dto.request.MenuUpdateRequestDto;
import com.example.outsourcing_11.domain.menu.dto.response.MenuAdminResponseDto;
import com.example.outsourcing_11.domain.menu.enums.Category;
import com.example.outsourcing_11.domain.menu.enums.MenuStatus;
import com.example.outsourcing_11.domain.menu.service.MenuAdminServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MenuAdminController.class)
class MenuAdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private MenuAdminServiceImpl menuAdminServiceImpl;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("POST /admin/{storeId}/menus - 메뉴 생성")
    @WithMockUser(roles = "사장님")
    void 메뉴_생성() throws Exception {
        MenuSaveRequestDto requestDto = new MenuSaveRequestDto(
            Category.MAIN_MENU,
            "김밥",
            "주문시 조리",
            new BigDecimal("3500"),
            MenuStatus.AVAILABLE
        );

        MenuAdminResponseDto responseDto = new MenuAdminResponseDto(
            1L,
            1L,
            Category.MAIN_MENU,
            "김밥",
            "주문시 조리",
            MenuStatus.AVAILABLE,
            LocalDateTime.now()
        );

        Mockito.when(menuAdminServiceImpl.createMenu(Mockito.eq(1L), Mockito.any(MenuSaveRequestDto.class), Mockito.any(HttpServletRequest.class)))
            .thenReturn(responseDto);

        mockMvc.perform(post("/admin/1/menus")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto))
                .with(csrf().asHeader()))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.menuId").value(1))
            .andExpect(jsonPath("$.menuName").value("김밥"))
            .andExpect(jsonPath("$.content").value("주문시 조리"))
            .andExpect(jsonPath("$.category").value("MAIN_MENU"))
            .andExpect(jsonPath("$.menuStatus").value("AVAILABLE"));
    }


    @Test
    @DisplayName("Patch /admin/{storeId}/menus/{menuId} - 메뉴 생성")
    @WithMockUser(roles = "사장님")
    void 메뉴_업데이트_일부_필드만_수정() throws Exception {
        Long storeId = 1L;
        Long menuId = 1L;
        MenuUpdateRequestDto requestDto = new MenuUpdateRequestDto(
            null,
            null,
            null,
            null,
            MenuStatus.SOLD_OUT
        );

        MenuAdminResponseDto responseDto = new MenuAdminResponseDto(
            menuId,
            storeId,
            Category.MAIN_MENU,
            "김밥",
            "주문시 조리",
            MenuStatus.SOLD_OUT,
            LocalDateTime.now()
        );


        Mockito.when(menuAdminServiceImpl.updateMenu(
                Mockito.eq(storeId),
                Mockito.eq(menuId),
                Mockito.any(MenuUpdateRequestDto.class),
                Mockito.any(HttpServletRequest.class)))
            .thenReturn(responseDto);

        // When & Then
        mockMvc.perform(patch("/admin/" + storeId + "/menus/" + menuId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto))
                .with(csrf().asHeader()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.menuId").value(menuId))
            .andExpect(jsonPath("$.storeId").value(storeId))
            .andExpect(jsonPath("$.menuName").value("김밥"))
            .andExpect(jsonPath("$.content").value("주문시 조리"))
            .andExpect(jsonPath("$.category").value("MAIN_MENU"))
            .andExpect(jsonPath("$.menuStatus").value("SOLD_OUT"));
    }


    @Test
    void deleteMenu() {
    }
}
