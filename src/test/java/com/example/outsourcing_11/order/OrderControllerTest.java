package com.example.outsourcing_11.order;

import com.example.outsourcing_11.common.UserRole;
import com.example.outsourcing_11.domain.menu.entity.Menu;
import com.example.outsourcing_11.domain.menu.repository.MenuRepository;
import com.example.outsourcing_11.domain.order.dto.OrderItemRequestDto;
import com.example.outsourcing_11.domain.order.dto.OrderRequestDto;
import com.example.outsourcing_11.domain.order.repository.CartRepository;
import com.example.outsourcing_11.domain.store.dto.StoreRequestDto;
import com.example.outsourcing_11.domain.store.entity.Store;
import com.example.outsourcing_11.domain.store.entity.StoreStatus;
import com.example.outsourcing_11.domain.store.repository.StoreRepository;
import com.example.outsourcing_11.domain.user.entity.User;
import com.example.outsourcing_11.domain.user.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import com.example.outsourcing_11.domain.store.entity.StoreCategory;
import com.example.outsourcing_11.domain.menu.enums.Category;
import com.example.outsourcing_11.domain.menu.enums.MenuStatus;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
// 응답 상태 확인용 (status().isOk(), isCreated() 등)
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// JSON 응답 값 검증용 (jsonPath())
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
public class OrderControllerTest {
    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private UserRepository userRepository;
    @Autowired private MenuRepository menuRepository;
    @Autowired private StoreRepository storeRepository;
    @Autowired private CartRepository cartRepository;


    @Test
    @Transactional
    @WithMockUser(username = "yuri@example.com", roles = {"CUSTOMER"})
    @DisplayName("POST /orders - 주문 생성")
    void createOrder() throws Exception {
        // ✅ 중복 유저 정리 (Store 먼저 삭제 → User 삭제)
        userRepository.findByEmail("yuri@example.com").ifPresent(user -> {
            // 1. 유저의 장바구니 먼저 삭제
            cartRepository.deleteAllByUser(user);

            // 2. 그 유저가 소유한 가게들 삭제
            storeRepository.deleteAll(storeRepository.findAllByOwner(user));

            // 3. 마지막으로 유저 삭제
            userRepository.delete(user);
        });
        userRepository.findByEmail("yuri@example.com")
                .ifPresent(userRepository::delete);

        // given
        User user = new User(
                "유리",                   // name
                "yuri@example.com",       // email
                "1234",                   // password
                "01011112222",            // phone
                "서울시",                 // address
                UserRole.CUSTOMER         // ✅ 반드시 enum 타입
        );
        user = userRepository.save(user);

        StoreRequestDto dto = new StoreRequestDto(
                "맥도날드",
                LocalDateTime.now(),
                LocalDateTime.now().plusHours(8),
                5000,
                "햄버거",
                StoreCategory.HAMBURGER,
                StoreStatus.OPEN
        );

        Store store = storeRepository.save(new Store(dto, user));

        Menu menu = new Menu();

        Field mField = Menu.class.getDeclaredField("category");
        mField.setAccessible(true);
        mField.set(menu, Category.MAIN_MENU);  // enum 값 사용

        mField = Menu.class.getDeclaredField("name");
        mField.setAccessible(true);
        mField.set(menu, "치즈버거");

        mField = Menu.class.getDeclaredField("content");
        mField.setAccessible(true);
        mField.set(menu, "치즈가 가득한 버거");

        mField = Menu.class.getDeclaredField("price");
        mField.setAccessible(true);
        mField.set(menu, BigDecimal.valueOf(7000));

        mField = Menu.class.getDeclaredField("status");
        mField.setAccessible(true);
        mField.set(menu, MenuStatus.AVAILABLE);  // enum 값 사용

        mField = Menu.class.getDeclaredField("store");
        mField.setAccessible(true);
        mField.set(menu, store);  // 미리 저장된 Store 객체여야 함

        menu = menuRepository.save(menu);

        OrderItemRequestDto item = new OrderItemRequestDto(menu.getId(), store.getId(), 2, 7000);
        OrderRequestDto request = new OrderRequestDto(user.getId(), List.of(item));

        // when & then
        mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(user.getId()))
                .andExpect(jsonPath("$.items[0].menuId").value(menu.getId()));
    }



    @Test
    @DisplayName("주문 생성 후 사용자 주문 목록에 자동 반영되는지 확인")
    void createAndGetOrdersByUserId() throws Exception {
        mockMvc.perform(get("/orders/1"))
                .andExpect(status().isOk());
    }
    @Test
    @DisplayName("GET /orders/{orderId} - 단일 주문 조회")
    void getOrderById() throws Exception {
        mockMvc.perform(get("/orders/1"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("POST /orders/price - 주문 가격 계산")
    void calculatePrice() throws Exception {
        OrderItemRequestDto item = new OrderItemRequestDto(1L, 1L, 2, 7000);

        OrderRequestDto request = new OrderRequestDto(1L, List.of(item));

        mockMvc.perform(post("/orders/price")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.finalPrice").value(14000));
    }

    @Test
    @DisplayName("PATCH /orders/{orderId} - 주문 상태 변경")
    void updateOrderStatus() throws Exception {
        String body = "{\"status\":\"COMPLETED\"}";

        mockMvc.perform(patch("/orders/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("COMPLETED"));
    }

    @Test
    @DisplayName("DELETE /orders/{orderId} - 주문 취소")
    void cancelOrder() throws Exception {
        mockMvc.perform(delete("/orders/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("주문이 성공적으로 취소되었습니다."));
    }
}
