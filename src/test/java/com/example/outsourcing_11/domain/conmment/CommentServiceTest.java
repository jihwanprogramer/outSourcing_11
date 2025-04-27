package com.example.outsourcing_11.domain.conmment;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import jakarta.persistence.EntityManager;
import jakarta.validation.constraints.DecimalMin;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import com.example.outsourcing_11.common.UserRole;
import com.example.outsourcing_11.config.security.JwtAuthenticationEntryPoint;
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
public class CommentServiceTest {

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
	@Autowired
	private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

	@Test
	@DisplayName("범위 서치")
	void findByrating_comment() {
		//given

		//User Owner
		User Owner = new User("시바","siba@test.com", "test@1er3", "010-1111-2222", "서울시 강남구",UserRole.OWNER);
		//User Customer
		List<User> Customer =List.of(
			new User("유리","siba@test.com", "test@1er3", "010-1111-2222", "서울시 강남구",UserRole.CUSTOMER),
			new User("형진","siba@test.com", "test@1er3", "010-1111-2222", "서울시 강남구",UserRole.CUSTOMER),
		    new User("지환","siba@test.com", "test@1er3", "010-1111-2222", "서울시 강남구",UserRole.CUSTOMER),
			new User("석진","siba@test.com", "test@1er3", "010-1111-2222", "서울시 강남구",UserRole.CUSTOMER),
			new User("은세","siba@test.com", "test@1er3", "010-1111-2222", "서울시 강남구",UserRole.CUSTOMER)
		);
		userRepository.save(Owner);
		userRepository.saveAll(Customer);

		//Store store
		List<Store> stores = List.of(
			new Store(
				"맘스 터치"
				,"경기도 동두천시"
				,LocalDateTime.of(2025, 4, 27, 9, 0,0)
				, LocalDateTime.of(2025, 4, 27, 21, 0,0)
			    ,16000
				,StoreStatus.OPEN
			    ,StoreCategory.BURGER
			    , Owner)
		);
		storeRepository.saveAll(stores);
		//Menu menu
		BigDecimal bigDecimal1 =new BigDecimal(8000);
		BigDecimal bigDecimal2 =new BigDecimal(800);
		BigDecimal bigDecimal3 =new BigDecimal(1500);

		List<Menu> menus = List.of(
			new Menu(Category.MAIN_MENU, "싸이 버거"," 맛있는 치킨이 들어있는 버거",  bigDecimal1, MenuStatus.AVAILABLE, stores.get(0) ),
			new Menu(Category.DRINK, "제로 콜리","펩시 라임맛 제로 콜리!", bigDecimal2, MenuStatus.AVAILABLE, stores.get(0) ),
			new Menu(Category.SIDE_MENU, "감자 튀김","영원한 햄버거 단짝 감자 튀김", bigDecimal3 , MenuStatus.AVAILABLE, stores.get(0) )
		);
		menuRepository.saveAll(menus);
		Order(User user, Store store, LocalDateTime orderDate, OrderStatus status, int totalPrice, List<OrderItem> items)

		OrderItem orderItem = new OrderItem()
		//Order order
		List<Order> orders = List.of(
             new Order(Customer.get(1), stores.get(0), LocalDateTime.now(), OrderStatus.PENDING, )
		);
		//Comment

		//when


		  //comment 추가
		  //별점  조회 테스트
		  //

		//then
		   //결과 출력

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

