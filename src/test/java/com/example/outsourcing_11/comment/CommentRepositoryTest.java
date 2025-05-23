package com.example.outsourcing_11.comment;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import jakarta.persistence.EntityManager;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import com.example.outsourcing_11.domain.comment.entity.Comment;
import com.example.outsourcing_11.domain.comment.repository.CommentRepository;
import com.example.outsourcing_11.domain.menu.entity.Menu;
import com.example.outsourcing_11.domain.menu.enums.Category;
import com.example.outsourcing_11.domain.menu.enums.MenuStatus;
import com.example.outsourcing_11.domain.menu.repository.MenuRepository;
import com.example.outsourcing_11.domain.order.entity.Order;
import com.example.outsourcing_11.domain.order.entity.OrderItem;
import com.example.outsourcing_11.domain.order.entity.OrderStatus;
import com.example.outsourcing_11.domain.order.repository.OrderRepository;
import com.example.outsourcing_11.domain.store.entity.Store;
import com.example.outsourcing_11.domain.store.entity.StoreCategory;
import com.example.outsourcing_11.domain.store.entity.StoreStatus;
import com.example.outsourcing_11.domain.store.repository.StoreRepository;
import com.example.outsourcing_11.domain.user.entity.User;
import com.example.outsourcing_11.domain.user.repository.UserRepository;

@ActiveProfiles("local")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CommentRepositoryTest {
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private StoreRepository storeRepository;
	@Autowired
	private MenuRepository menuRepository;
	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private CommentRepository commentRepository;
	@Autowired
	private EntityManager em;

	@Test
	@DisplayName("범위 서치")
	void findByrating_comment() {
		//given

		List<User> users = List.of(
			new User("유리", "yuri@test.com", "1234", "01012345678", "USER", "서울 강남구"),
			new User("형진", "test1@test.com", "1234", "01012345678", "USER", "서울 강남구"),
			new User("지환", "test2@test.com", "1234", "01012345678", "USER", "서울 강남구"),
			new User("은세", "test3@test.com", "1234", "01012345678", "USER", "서울 강남구")
		);
		users.forEach(em::persist);

		List<Store> stores = new ArrayList<>();
		for (int i = 0; i < users.size(); i++) {
			stores.add(new Store("맘스터치", "서울시 강남구", 16000, StoreCategory.BURGER, StoreStatus.OPEN, users.get(i)));
		}
		stores.forEach(em::persist);

		BigDecimal price = new BigDecimal("8000");

		List<Menu> menus = new ArrayList<>();
		for (int i = 0; i < users.size(); i++) {
			menus.add(new Menu(Category.MAIN_MENU, "치즈 버거", "맛있는 버거", price, MenuStatus.AVAILABLE, stores.get(i)));
		}
		menus.forEach(em::persist);

		List<Order> orders = new ArrayList<>();
		for (int i = 0; i < users.size(); i++) {

			Order order = new Order(users.get(i), LocalDateTime.now(), OrderStatus.PENDING, 16000);

			OrderItem item = new OrderItem();
			item.setOrder(order);
			item.setMenu(menus.get(i));
			item.setStore(stores.get(i));
			item.setQuantity(2);
			item.setItemPrice(16000);
			order.getItems().add(item);
			orders.add(order);
		}

		// When
		for (int i = 0; i < users.size(); i++) {
			orderRepository.save(orders.get(i));
			userRepository.save(users.get(i));
			storeRepository.save(stores.get(i));
		}
		Random random = new Random();

		for (int i = 0; i < users.size(); i++) {
			int num = random.nextInt(0, 5);
			Comment comment = commentRepository.save(new Comment("맛있습니다!", null, num));

		}
		List<Comment> found = commentRepository.findByRatingBetweenAndDeletedAtIsNull(0, 5);
		//then
		for (int i = 0; i < found.size(); i++) {
			assertThat(found.get(i).getId());
			System.out.println(found.get(i).getUser().getName());
			System.out.println(found.get(i).getRating());
		}

	}

	@Test
	@DisplayName("comment 정상 추가 테스트")
	void findById_comment() {
		//given
		User user = new User("유리", "yuri@test.com", "1234", "01012345678", "USER", "서울 강남구");
		em.persist(user);

		Store store = new Store("맘스터치", "서울시 강남구", 16000, StoreCategory.BURGER, StoreStatus.OPEN, user);
		em.persist(store);

		BigDecimal price = new BigDecimal("8000");
		Menu menu = new Menu(Category.MAIN_MENU, "치즈 버거", "맛있는 버거", price, MenuStatus.AVAILABLE, store);
		em.persist(menu);

		Order order = new Order(user, LocalDateTime.now(), OrderStatus.PENDING, 16000);

		OrderItem item = new OrderItem();
		item.setOrder(order);
		item.setMenu(menu);
		item.setStore(store);
		item.setQuantity(2);
		item.setItemPrice(16000);

		order.getItems().add(item);
		// When
		userRepository.save(user);
		storeRepository.save(store);
		menuRepository.save(menu);
		orderRepository.save(order);
		Comment comment = commentRepository.save(new Comment("맛있습니다!", null, 5));
		comment.updateUserAndStore(user, store);
		//when
		Optional<Comment> found = commentRepository.findById(comment.getId());

		//then
		assertThat(found).isPresent();
		assertThat(found.get().getId()).isEqualTo(comment.getId());
	}
}
