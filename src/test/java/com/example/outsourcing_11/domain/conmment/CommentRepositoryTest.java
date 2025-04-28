package com.example.outsourcing_11.domain.conmment;

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
import com.example.outsourcing_11.domain.store.entity.Store;
import com.example.outsourcing_11.domain.store.entity.StoreCategory;
import com.example.outsourcing_11.domain.store.entity.StoreStatus;
import com.example.outsourcing_11.domain.store.repository.StoreRepository;
import com.example.outsourcing_11.domain.user.entity.User;
import com.example.outsourcing_11.domain.user.repository.UserRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

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
    @DisplayName("리뷰에서 주문, 주문한 가계, 주문한 유저를 확인하기.")
    void commentGetOrderInfo() {
        //given

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
                , LocalTime.of(9, 0, 0)
                , LocalTime.of(21, 0, 0)
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
        Order order = new Order(customer.get(1), stores.get(0), LocalDateTime.now(), OrderStatus.COMPLETED, 8000,
            items);

        Random randomMenu = new Random();
        for (int j = 0; j < 3; j++) {
            int menuNum = randomMenu.nextInt(0, 3);
            OrderItem item = new OrderItem();
            item.setMenu(menus.get(menuNum));
            item.setStore(stores.get(0));
            item.setQuantity(1);
            item.setOrder(order);
            items.add(item);
        }

        orderRepository.save(order);
        orderRepository.flush();

        Comment comment = new Comment("맛있습니다.", null, 3);
        comment.updateOrder(order);
        commentRepository.save(comment);
        commentRepository.flush();
        //when
        Optional<Comment> found = commentRepository.findById(comment.getId());
        //then
        assertThat(found).isPresent();
        assertThat(found.get().getId()).isEqualTo(comment.getId());
        if (found.isPresent()) {
            System.out.println(found.get().getId());
            System.out.println(found.get().getUser().getId());
            System.out.println(found.get().getUser().getName());
            System.out.println(found.get().getUser().getAddress());
            System.out.println(found.get().getUser().getPhone());
            System.out.println(found.get().getUser().getRole());
            System.out.println(found.get().getUser().getStatus());

        }
    }

    @Test
    @DisplayName("리뷰의 별점 범위에 따른 리뷰 조회하기.")
    void findByrating_comment() {
        //given

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
                , LocalTime.of(9, 0, 0)
                , LocalTime.of(21, 0, 0)
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

        Random randomRating = new Random();
        List<Comment> comments = new ArrayList<>();
        for (int i = 0; i < customer.size(); i++) {
            int rating = randomRating.nextInt(0, 5);
            Comment comment = new Comment("맛있습니다.", null, rating);
            comment.updateOrder(orders.get(i));

            comments.add(comment);
        }
        commentRepository.saveAll(comments);
        commentRepository.flush();

        //when
        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Comment> pageComment = commentRepository.findByRatingBetweenAndDeletedAtIsNull(0, 5, pageRequest);
        //then
        assertThat(pageComment).isNotNull();
        assertThat(pageComment.getContent().size()).isLessThanOrEqualTo(5);
        pageComment.getContent()
            .forEach(c -> System.out.println(String.format("내용: %s, 별점: %d", c.getContent(), c.getRating())));

    }

    @Test
    @DisplayName("주문의 댓글 조회.")
    void findByOrdercomment() {
        //given

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
                , LocalTime.of(9, 0, 0)
                , LocalTime.of(21, 0, 0)
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

        String[] content = new String[]{"맛있어요", "서비스가 별루에요", "너무 맛있었습니다. 다음에도 주문 할래요!"};
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

        //when
        Optional<Comment> optionalComment = commentRepository.findWithRelationsByOrderId(
            comments.get(0).getOrder().getId());
        //then
        assertThat(optionalComment).isPresent();
        if (optionalComment.isPresent()) {
            comments.stream()
                .forEach(comment -> {
                    // comment의 원하는 필드를 출력
                    System.out.println("내용: " + comment.getContent() + ", 별점: " + comment.getRating() + ", 날짜"
                        + comment.getCreatedAt().toString());
                });
        }
    }
}

