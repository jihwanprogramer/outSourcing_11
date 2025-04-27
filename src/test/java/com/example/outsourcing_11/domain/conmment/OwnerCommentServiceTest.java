package com.example.outsourcing_11.domain.conmment;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import com.example.outsourcing_11.common.UserRole;
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
import com.example.outsourcing_11.domain.ownercomment.dto.OwnerRequestCommentDto;
import com.example.outsourcing_11.domain.ownercomment.dto.OwnerResponseCommentDto;
import com.example.outsourcing_11.domain.ownercomment.repository.OwnerCommentRepository;
import com.example.outsourcing_11.domain.ownercomment.service.OwnerServiceimple;
import com.example.outsourcing_11.domain.store.entity.Store;
import com.example.outsourcing_11.domain.store.entity.StoreCategory;
import com.example.outsourcing_11.domain.store.entity.StoreStatus;
import com.example.outsourcing_11.domain.store.repository.StoreRepository;
import com.example.outsourcing_11.domain.user.entity.User;
import com.example.outsourcing_11.domain.user.repository.UserRepository;

@ActiveProfiles("local")
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class OwnerCommentServiceTest {

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
	private OwnerCommentRepository ownerCommentRepository;
	@Autowired
	private OwnerServiceimple ownerServiceimple;

	@Test
	@DisplayName("사장님 대댓글 작성 성공")
	void commentGetOrderInfo() {
		//기본 셋팅

		//User Owner
		User owner = new User("시바", "siba@test.com", "test@1er3", "010-1111-2222", "서울시 강남구", UserRole.OWNER);
		//User Customer
		List<User> customer = List.of(
			new User("유리", "test1@test.com", "test@1er3", "010-1111-2222", "서울시 강남구", UserRole.CUSTOMER),
			new User("형진", "test2@test.com", "test@1er3", "010-1111-2222", "서울시 강남구", UserRole.CUSTOMER),
			new User("지환", "test3@test.com", "test@1er3", "010-1111-2222", "서울시 강남구", UserRole.CUSTOMER),
			new User("석진", "test4@test.com", "test@1er3", "010-1111-2222", "서울시 강남구", UserRole.CUSTOMER),
			new User("은세", "test5@test.com", "test@1er3", "010-1111-2222", "서울시 강남구", UserRole.CUSTOMER)
		);
		userRepository.save(owner);
		userRepository.saveAll(customer);
		userRepository.flush();

		//Store store
		List<Store> stores = List.of(
			new Store(
				"맘스 터치"
				, "경기도 동두천시"
				, LocalDateTime.of(2025, 4, 27, 9, 0, 0)
				, LocalDateTime.of(2025, 4, 27, 21, 0, 0)
				, 16000
				, StoreStatus.OPEN
				, StoreCategory.BURGER
				, owner)
		);
		storeRepository.saveAll(stores);
		storeRepository.flush();
		//Menu menu
		BigDecimal bigDecimal1 = new BigDecimal(8000);
		BigDecimal bigDecimal2 = new BigDecimal(800);
		BigDecimal bigDecimal3 = new BigDecimal(1500);

		List<Menu> menus = List.of(
			new Menu(Category.MAIN_MENU, "싸이 버거", " 맛있는 치킨이 들어있는 버거", bigDecimal1, MenuStatus.AVAILABLE, stores.get(0)),
			new Menu(Category.DRINK, "제로 콜리", "펩시 라임맛 제로 콜리!", bigDecimal2, MenuStatus.AVAILABLE, stores.get(0)),
			new Menu(Category.SIDE_MENU, "감자 튀김", "영원한 햄버거 단짝 감자 튀김", bigDecimal3, MenuStatus.AVAILABLE, stores.get(0))
		);
		menuRepository.saveAll(menus);
		menuRepository.flush();

		List<OrderItem> items = new ArrayList<>();
		Random randomMenu = new Random();
		for (int j = 0; j < 3; j++) {
			int menuNum = randomMenu.nextInt(0, 3);
			OrderItem item = new OrderItem();
			item.setMenu(menus.get(menuNum));
			item.setStore(stores.get(0));
			item.setQuantity(1);
			items.add(item);
		}
		Order order = new Order(customer.get(1), stores.get(0), LocalDateTime.now(), OrderStatus.COMPLETED, 8000,
			items);
		orderRepository.save(order);
		orderRepository.flush();

		Comment comment = new Comment("맛있습니다.", null, 3);
		comment.updateOrder(order);
		commentRepository.save(comment);
		commentRepository.flush();

		//given
		OwnerRequestCommentDto ownerRequestCommentDto = new OwnerRequestCommentDto("감사합니다.");

		//when
		OwnerResponseCommentDto getOwnerComment =
			ownerServiceimple.createOwerComment(comment.getId(), ownerRequestCommentDto);
		//then
		assertThat(getOwnerComment).isNotNull();
		System.out.println("content" + getOwnerComment.getContent());
	}

	@Test
	void 사장님_대댓글_수정_성공() {
		//기본 셋팅

		//User Owner
		User owner = new User("시바", "siba@test.com", "test@1er3", "010-1111-2222", "서울시 강남구", UserRole.OWNER);
		//User Customer
		List<User> customer = List.of(
			new User("유리", "test1@test.com", "test@1er3", "010-1111-2222", "서울시 강남구", UserRole.CUSTOMER),
			new User("형진", "test2@test.com", "test@1er3", "010-1111-2222", "서울시 강남구", UserRole.CUSTOMER),
			new User("지환", "test3@test.com", "test@1er3", "010-1111-2222", "서울시 강남구", UserRole.CUSTOMER),
			new User("석진", "test4@test.com", "test@1er3", "010-1111-2222", "서울시 강남구", UserRole.CUSTOMER),
			new User("은세", "test5@test.com", "test@1er3", "010-1111-2222", "서울시 강남구", UserRole.CUSTOMER)
		);
		userRepository.save(owner);
		userRepository.saveAll(customer);
		userRepository.flush();

		//Store store
		List<Store> stores = List.of(
			new Store(
				"맘스 터치"
				, "경기도 동두천시"
				, LocalDateTime.of(2025, 4, 27, 9, 0, 0)
				, LocalDateTime.of(2025, 4, 27, 21, 0, 0)
				, 16000
				, StoreStatus.OPEN
				, StoreCategory.BURGER
				, owner)
		);
		storeRepository.saveAll(stores);
		storeRepository.flush();
		//Menu menu
		BigDecimal bigDecimal1 = new BigDecimal(8000);
		BigDecimal bigDecimal2 = new BigDecimal(800);
		BigDecimal bigDecimal3 = new BigDecimal(1500);

		List<Menu> menus = List.of(
			new Menu(Category.MAIN_MENU, "싸이 버거", " 맛있는 치킨이 들어있는 버거", bigDecimal1, MenuStatus.AVAILABLE, stores.get(0)),
			new Menu(Category.DRINK, "제로 콜리", "펩시 라임맛 제로 콜리!", bigDecimal2, MenuStatus.AVAILABLE, stores.get(0)),
			new Menu(Category.SIDE_MENU, "감자 튀김", "영원한 햄버거 단짝 감자 튀김", bigDecimal3, MenuStatus.AVAILABLE, stores.get(0))
		);
		menuRepository.saveAll(menus);
		menuRepository.flush();

		List<OrderItem> items = new ArrayList<>();
		Random randomMenu = new Random();
		for (int j = 0; j < 3; j++) {
			int menuNum = randomMenu.nextInt(0, 3);
			OrderItem item = new OrderItem();
			item.setMenu(menus.get(menuNum));
			item.setStore(stores.get(0));
			item.setQuantity(1);
			items.add(item);
		}
		Order order = new Order(customer.get(1), stores.get(0), LocalDateTime.now(), OrderStatus.COMPLETED, 8000,
			items);
		orderRepository.save(order);
		orderRepository.flush();

		Comment comment = new Comment("맛있습니다.", null, 3);
		comment.updateOrder(order);
		commentRepository.save(comment);
		commentRepository.flush();

		//given & when
		OwnerRequestCommentDto ownerRequestCommentDto = new OwnerRequestCommentDto("감사합니다.");
		OwnerResponseCommentDto getOwnerComment =
			ownerServiceimple.createOwerComment(comment.getId(), ownerRequestCommentDto);

		OwnerRequestCommentDto updateOwnerRequestCommentDto = new OwnerRequestCommentDto("먹질 말아!");
		OwnerResponseCommentDto updateOwnerComment =
			ownerServiceimple.updateOwnerComment(comment.getId(), updateOwnerRequestCommentDto);
		//then
		assertThat(getOwnerComment).isNotNull();
		System.out.println("content" + getOwnerComment.getContent());
		assertThat(updateOwnerComment).isNotNull();
		System.out.println("content" + updateOwnerComment.getContent());
	}

	@Test
	void 사장님_대댓글_삭제_성공() {
		//기본 셋팅

		//User Owner
		User owner = new User("시바", "siba@test.com", "test@1er3", "010-1111-2222", "서울시 강남구", UserRole.OWNER);
		//User Customer
		List<User> customer = List.of(
			new User("유리", "test1@test.com", "test@1er3", "010-1111-2222", "서울시 강남구", UserRole.CUSTOMER),
			new User("형진", "test2@test.com", "test@1er3", "010-1111-2222", "서울시 강남구", UserRole.CUSTOMER),
			new User("지환", "test3@test.com", "test@1er3", "010-1111-2222", "서울시 강남구", UserRole.CUSTOMER),
			new User("석진", "test4@test.com", "test@1er3", "010-1111-2222", "서울시 강남구", UserRole.CUSTOMER),
			new User("은세", "test5@test.com", "test@1er3", "010-1111-2222", "서울시 강남구", UserRole.CUSTOMER)
		);
		userRepository.save(owner);
		userRepository.saveAll(customer);
		userRepository.flush();

		//Store store
		List<Store> stores = List.of(
			new Store(
				"맘스 터치"
				, "경기도 동두천시"
				, LocalDateTime.of(2025, 4, 27, 9, 0, 0)
				, LocalDateTime.of(2025, 4, 27, 21, 0, 0)
				, 16000
				, StoreStatus.OPEN
				, StoreCategory.BURGER
				, owner)
		);
		storeRepository.saveAll(stores);
		storeRepository.flush();
		//Menu menu
		BigDecimal bigDecimal1 = new BigDecimal(8000);
		BigDecimal bigDecimal2 = new BigDecimal(800);
		BigDecimal bigDecimal3 = new BigDecimal(1500);

		List<Menu> menus = List.of(
			new Menu(Category.MAIN_MENU, "싸이 버거", " 맛있는 치킨이 들어있는 버거", bigDecimal1, MenuStatus.AVAILABLE, stores.get(0)),
			new Menu(Category.DRINK, "제로 콜리", "펩시 라임맛 제로 콜리!", bigDecimal2, MenuStatus.AVAILABLE, stores.get(0)),
			new Menu(Category.SIDE_MENU, "감자 튀김", "영원한 햄버거 단짝 감자 튀김", bigDecimal3, MenuStatus.AVAILABLE, stores.get(0))
		);
		menuRepository.saveAll(menus);
		menuRepository.flush();

		List<OrderItem> items = new ArrayList<>();
		Random randomMenu = new Random();
		for (int j = 0; j < 3; j++) {
			int menuNum = randomMenu.nextInt(0, 3);
			OrderItem item = new OrderItem();
			item.setMenu(menus.get(menuNum));
			item.setStore(stores.get(0));
			item.setQuantity(1);
			items.add(item);
		}
		Order order = new Order(customer.get(1), stores.get(0), LocalDateTime.now(), OrderStatus.COMPLETED, 8000,
			items);
		orderRepository.save(order);
		orderRepository.flush();

		Comment comment = new Comment("맛있습니다.", null, 3);
		comment.updateOrder(order);
		commentRepository.save(comment);
		commentRepository.flush();

		//given & when
		OwnerRequestCommentDto ownerRequestCommentDto = new OwnerRequestCommentDto("감사합니다.");
		OwnerResponseCommentDto getOwnerComment =
			ownerServiceimple.createOwerComment(comment.getId(), ownerRequestCommentDto);

		OwnerRequestCommentDto updateOwnerRequestCommentDto = new OwnerRequestCommentDto("먹질 말아!");

		OwnerResponseCommentDto updateOwnerComment =
			ownerServiceimple.updateOwnerComment(comment.getId(), updateOwnerRequestCommentDto);
		//System.out.println("content" + comment.getOwnerComment().getContent()); <- 이 타이밍에서 Null why?

		//then
		assertThat(getOwnerComment).isNotNull();
		System.out.println("content" + getOwnerComment.getContent());
		assertThat(updateOwnerComment).isNotNull();

		assertThatCode(() -> ownerServiceimple.deleteOwerComment(comment.getId())).doesNotThrowAnyException();

	}

	@Test
	void 사장님_대댓글_전체보기() {
		//기본 셋팅

		//User Owner
		User owner = new User("시바", "siba@test.com", "test@1er3", "010-1111-2222", "서울시 강남구", UserRole.OWNER);
		//User Customer
		List<User> customer = List.of(
			new User("유리", "test1@test.com", "test@1er3", "010-1111-2222", "서울시 강남구", UserRole.CUSTOMER),
			new User("형진", "test2@test.com", "test@1er3", "010-1111-2222", "서울시 강남구", UserRole.CUSTOMER),
			new User("지환", "test3@test.com", "test@1er3", "010-1111-2222", "서울시 강남구", UserRole.CUSTOMER),
			new User("석진", "test4@test.com", "test@1er3", "010-1111-2222", "서울시 강남구", UserRole.CUSTOMER),
			new User("은세", "test5@test.com", "test@1er3", "010-1111-2222", "서울시 강남구", UserRole.CUSTOMER)
		);
		userRepository.save(owner);
		userRepository.saveAll(customer);
		userRepository.flush();

		//Store store
		List<Store> stores = List.of(
			new Store(
				"맘스 터치"
				, "경기도 동두천시"
				, LocalDateTime.of(2025, 4, 27, 9, 0, 0)
				, LocalDateTime.of(2025, 4, 27, 21, 0, 0)
				, 16000
				, StoreStatus.OPEN
				, StoreCategory.BURGER
				, owner)
		);
		storeRepository.saveAll(stores);
		storeRepository.flush();
		//Menu menu
		BigDecimal bigDecimal1 = new BigDecimal(8000);
		BigDecimal bigDecimal2 = new BigDecimal(800);
		BigDecimal bigDecimal3 = new BigDecimal(1500);

		List<Menu> menus = List.of(
			new Menu(Category.MAIN_MENU, "싸이 버거", " 맛있는 치킨이 들어있는 버거", bigDecimal1, MenuStatus.AVAILABLE, stores.get(0)),
			new Menu(Category.DRINK, "제로 콜리", "펩시 라임맛 제로 콜리!", bigDecimal2, MenuStatus.AVAILABLE, stores.get(0)),
			new Menu(Category.SIDE_MENU, "감자 튀김", "영원한 햄버거 단짝 감자 튀김", bigDecimal3, MenuStatus.AVAILABLE, stores.get(0))
		);
		menuRepository.saveAll(menus);
		menuRepository.flush();
		Map<Integer, List<OrderItem>> orderList = new HashMap<>();

		Random randomMenu = new Random();

		for (int i = 0; i < customer.size(); i++) {
			List<OrderItem> items = new ArrayList<>();

			for (int j = 0; j < 3; j++) {
				int menuNum = randomMenu.nextInt(0, 3);
				OrderItem item = new OrderItem();
				item.setMenu(menus.get(menuNum));
				item.setStore(stores.get(0));
				item.setQuantity(1);
				items.add(item);
			}
			orderList.put(i, items);
		}

		List<Order> orders = new ArrayList<>();
		for (int i = 0; i < customer.size(); i++) {
			orders.add(new Order(customer.get(i), stores.get(0), LocalDateTime.now(), OrderStatus.COMPLETED, 8000,
				orderList.get(i)));
		}

		orderRepository.saveAll(orders);
		orderRepository.flush();

		String[] content = new String[] {"맛있어요", "서비스가 별루에요", "너무 맛있었습니다. 다음에도 주문 할래요!"};
		Random randomRating = new Random();
		Random randomContent = new Random();
		List<Comment> comments = new ArrayList<>();
		for (int i = 0; i < customer.size(); i++) {
			int rating = randomRating.nextInt(0, 5);
			int contentNum = randomContent.nextInt(0, content.length - 1);
			Comment comment = new Comment(content[contentNum], null, rating);
			comment.updateOrder(orders.get(i));

			comments.add(comment);
		}
		commentRepository.saveAll(comments);
		commentRepository.flush();

		// 사장님 댓글 등록
		for (int i = 0; i < customer.size(); i++) {
			OwnerRequestCommentDto ownerRequestCommentDto = new OwnerRequestCommentDto("감사합니다.");
			ownerServiceimple.createOwerComment(comments.get(i).getId(), ownerRequestCommentDto);
		}

		// 사장님 댓글 조회하고 출력
		for (int i = 0; i < customer.size(); i++) {
			ownerServiceimple.getOwnerComments(comments.get(i).getStore().getId(), comments.get(i).getId())
				.stream()
				.map(c -> "내용: " + c.getContent())
				.forEach(System.out::println);
		}

	}
}
