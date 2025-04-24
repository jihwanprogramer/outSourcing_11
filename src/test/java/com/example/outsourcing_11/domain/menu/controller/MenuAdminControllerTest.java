package com.example.outsourcing_11.domain.menu.controller;

import com.example.outsourcing_11.domain.menu.dto.request.MenuSaveRequestDto;
import com.example.outsourcing_11.domain.menu.dto.response.MenuAdminResponseDto;
import com.example.outsourcing_11.domain.menu.enums.Category;
import com.example.outsourcing_11.domain.menu.enums.MenuStatus;
import com.example.outsourcing_11.domain.menu.service.MenuAdminServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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
    void 메뉴_생성_성공() throws Exception {
        MenuSaveRequestDto dto = new MenuSaveRequestDto(
            Category.MAIN_MENU,
            "김밥",
            "주문시 조리",
            new BigDecimal("3500"),
            MenuStatus.AVAILABLE
        );

        MenuAdminResponseDto responseDto = new MenuAdminResponseDto(
            1L,
            Category.MAIN_MENU,
            "김밥",
            "주문시 조리",
            MenuStatus.AVAILABLE,
            LocalDateTime.now() // 또는 정해진 시간으로 고정 가능
        );

        Mockito.when(menuAdminServiceImpl.createMenu(Mockito.eq(1L), Mockito.any(MenuSaveRequestDto.class)))
            .thenReturn(responseDto);

        mockMvc.perform(post("/admin/1/menus")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.menuId").value(1))
            .andExpect(jsonPath("$.menuName").value("김밥"))
            .andExpect(jsonPath("$.content").value("주문시 조리"))
            .andExpect(jsonPath("$.category").value("MAIN_MENU"))
            .andExpect(jsonPath("$.menuStatus").value("AVAILABLE"));
    }

    @Test
    void 메뉴_생성_실패_가게없음() throws Exception {
        MenuSaveRequestDto dto = new MenuSaveRequestDto(
            Category.MAIN_MENU,
            "김밥",
            "주문시 조리",
            new BigDecimal("3500"),
            MenuStatus.AVAILABLE
        );

        // 가게가 존재하지 않으면 예외 발생
        Mockito.when(menuAdminServiceImpl.createMenu(Mockito.eq(999L), Mockito.any(MenuSaveRequestDto.class)))
            .thenThrow(new NullPointerException("해당 가게를 찾을 수 없습니다."));

        mockMvc.perform(post("/admin/999/menus")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isInternalServerError())
            .andExpect(jsonPath("$.message").value("해당 가게를 찾을 수 없습니다."));  // 응답 메시지 확인
    }


    @Test
    void updateMenu() {

    }

    @Test
    void deleteMenu() {
    }
}
