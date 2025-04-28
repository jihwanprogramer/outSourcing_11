package com.example.outsourcing_11.domain.menu.service;

import com.example.outsourcing_11.domain.menu.repository.MenuRepository;
import com.example.outsourcing_11.domain.store.repository.StoreRepository;
import com.example.outsourcing_11.util.JwtUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

class MenuAdminServiceImplTest {

    @Mock
    private MenuRepository menuRepository;

    @Mock
    private StoreRepository storeRepository;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private MenuAdminServiceImpl menuAdminServiceImpl;

    @Test
    @DisplayName("POST /admin/{storeId}/menus - 메뉴 생성")
    void 메뉴_생성_성공() {
//        Long storeId = 1L;
//        MenuSaveRequestDto requestDto = new MenuSaveRequestDto(
//            Category.MAIN_MENU,
//            "김밥",
//            "주문시 조리",
//            new BigDecimal("3500"),
//            MenuStatus.AVAILABLE
//        );
//
//        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
//        String token = "testToken";
//        Mockito.when(request.getHeader("Authorization")).thenReturn("Bearer "+ token);
//        Mockito.when(jwtUtil.extractUserId(token)).thenReturn(1L);
//
//        Mockito.when(storeRepository.findByIdAndOwnerIdAndDeletedFalse(storeId,1L)).
//            thenReturn()
    }

    @Test
    void updateMenu() {
    }

    @Test
    void delete() {
    }
}
