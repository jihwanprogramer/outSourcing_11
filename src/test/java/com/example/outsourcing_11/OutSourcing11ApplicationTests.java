package com.example.outsourcing_11;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import com.example.outsourcing_11.domain.menu.entity.Menu;
import com.example.outsourcing_11.domain.menu.enums.Category;
import com.example.outsourcing_11.domain.menu.enums.MenuStatus;
import com.example.outsourcing_11.domain.order.entity.Order;
import com.example.outsourcing_11.domain.order.entity.OrderItem;
import com.example.outsourcing_11.domain.order.entity.OrderStatus;
import com.example.outsourcing_11.domain.order.repository.OrderRepository;
import com.example.outsourcing_11.domain.store.entity.Store;
import com.example.outsourcing_11.domain.store.entity.StoreCategory;
import com.example.outsourcing_11.domain.store.entity.StoreStatus;
import com.example.outsourcing_11.domain.user.entity.User;

@SpringBootTest
class OutSourcing11ApplicationTests {

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private EntityManager em;

	@Transactional
	@Test
	@DisplayName("주문 생성 및 조회 테스트")
	void createAndFindOrder() {
		// Given
		User user = new User("유리", "yuri@test.com", "1234", "01012345678", "USER", "서울 강남구");
		em.persist(user);

		Store store = new Store("맘스터치", "서울시 강남구", 16000, StoreCategory.BURGER, StoreStatus.OPEN, user);
		em.persist(store);
		BigDecimal price = new BigDecimal("8000");
		Menu menu = new Menu(Category.MAIN_MENU, "치즈 버거", "맛있는 버거", price, MenuStatus.AVAILABLE, store);
		em.persist(menu);

		Order order = new Order(user, LocalDateTime.now(), OrderStatus.PENDING, (16000));

		OrderItem item = new OrderItem();
		item.setOrder(order);
		item.setMenu(menu);
		// item.setStore(store);
		item.setQuantity(2);
		item.setItemPrice(16000);

		order.getItems().add(item);

		// When
		Order saved = orderRepository.save(order);

		// Then
		Order found = orderRepository.findById(saved.getId()).orElseThrow();
		assertThat(found.getUser().getName()).isEqualTo("유리");
		assertThat(found.getItems().get(0).getMenu().getName()).isEqualTo("불고기버거");
		assertThat(found.getTotalPrice()).isEqualTo(16000);
	}

}
