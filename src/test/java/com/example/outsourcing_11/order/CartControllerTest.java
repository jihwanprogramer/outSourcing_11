package com.example.outsourcing_11.order;

import com.example.outsourcing_11.domain.menu.entity.Menu;
import com.example.outsourcing_11.domain.menu.enums.Category;
import com.example.outsourcing_11.domain.menu.enums.MenuStatus;
import com.example.outsourcing_11.domain.menu.repository.MenuRepository;
import com.example.outsourcing_11.domain.order.dto.CartItemRequestDto;
import com.example.outsourcing_11.domain.order.dto.CartRequestDto;
import com.example.outsourcing_11.domain.store.entity.Store;
import com.example.outsourcing_11.domain.store.entity.StoreCategory;
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
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@SpringBootTest
@AutoConfigureMockMvc
public class CartControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private UserRepository userRepository;
    @Autowired private MenuRepository menuRepository;
    @Autowired private StoreRepository storeRepository;

    @Test
    @DisplayName("장바구니에 항목 추가 - POST /carts/items")
    void addItemToCart() throws Exception {
        // given: 사용자, 가게, 메뉴 생성
        User user = userRepository.save(new User("유리", "yuri@example.com", "1234", "01011112222", "USER", "서울시"));

        Store store = storeRepository.save(Store.builder()
                .name("맥도날드")
                .address("서울시")
                .openTime(LocalDateTime.now())
                .closeTime(LocalDateTime.now().plusHours(8))
                .minimumOrderPrice(5000)
                .status(StoreStatus.OPEN)
                .category(StoreCategory.HAMBURGER)
                .owner(user)
                .build());

        Menu menu = menuRepository.save(new Menu(Category.MAIN_MENU, "치즈버거", "치즈가 가득한 버거",
                BigDecimal.valueOf(7000), MenuStatus.AVAILABLE, store));

        // when: 장바구니에 항목 추가 요청
        CartItemRequestDto requestDto = CartItemRequestDto.builder()
                .userId(user.getId())
                .menuId(menu.getId())
                .storeId(store.getId())
                .quantity(2)
                .build();

        mockMvc.perform(post("/carts/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(user.getId()))
                .andExpect(jsonPath("$.items[0].menuId").value(menu.getId()))
                .andExpect(jsonPath("$.items[0].quantity").value(2));
    }
    @Test
    @DisplayName("GET /carts/{userId} - 사용자 장바구니 조회")
    void getCartByUserId() throws Exception {
        User user = userRepository.save(new User("유리", "yuri@example.com", "1234", "01011112222", "USER", "서울시"));

        mockMvc.perform(get("/carts/" + user.getId()))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("POST /carts - 새로운 장바구니 생성")
    void createCart() throws Exception {
        User user = userRepository.save(new User("유리", "yuri@example.com", "1234", "01011112222", "USER", "서울시"));

        CartRequestDto request = new CartRequestDto(user.getId());

        mockMvc.perform(post("/carts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print()) // 이 줄 추가!!
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(user.getId()));
    }

}
