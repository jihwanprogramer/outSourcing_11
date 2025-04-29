package com.example.outsourcing_11.domain.conmment;

import com.example.outsourcing_11.common.UserRole;
import com.example.outsourcing_11.domain.comment.dto.RequestCommentDto;
import com.example.outsourcing_11.domain.comment.dto.ResponseCommentDto;
import com.example.outsourcing_11.domain.comment.entity.Comment;
import com.example.outsourcing_11.domain.comment.repository.CommentRepository;
import com.example.outsourcing_11.domain.comment.service.CommentServiceImple;
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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ActiveProfiles("local")
@SpringBootTest
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
    private CommentServiceImple commentServiceImple;

    @Test
    @DisplayName("Comment 수정")
    void CreateCommentTest() {
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
        //--- 기본 셋팅 끝 -//

        //given
        Comment Updatecomment = new Comment("맛없습니다..", null, 1);
        RequestCommentDto requestCommentDto = new RequestCommentDto(Updatecomment.getContent(), comment.getRating(),
            Updatecomment.getImageUrl());

        //when - 수정 작업
        System.out.println(order.getId());
        ResponseCommentDto getComment = commentServiceImple.updateComment(order.getId(),
            comment.getUser().getId(), comment.getId(), requestCommentDto);
        //then
        assertThat(getComment).isNotNull();

        System.out.println(getComment.getContent());
        System.out.println(getComment.getImageUrl());
        System.out.println(getComment.getRating());
        System.out.println(getComment.getCreatedAt());
        System.out.println(getComment.getModifiedAt());
    }

    @Test
    @DisplayName("Comment 삭제")
    void DeleteCommentTest() {
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

        Comment comment = new Comment("맛없습니다..", null, 1);
        comment.updateOrder(order);
        commentRepository.save(comment);
        commentRepository.flush();
        //--- 기본 셋팅 끝 -//

        //given
        // Comment Updatecomment = new Comment("맛없습니다..", null, 1);
        // RequestCommentDto requestCommentDto = new RequestCommentDto(Updatecomment.getContent(), comment.getRating(),
        // 	Updatecomment.getImageUrl());

        //when&then
        System.out.println(order.getId());
        System.out.println(comment.getUser().getId());
        System.out.println(order.getId());
        assertThatCode(() -> commentServiceImple.deleteComment(order.getId(), comment.getUser().getId(),
            comment.getId())).doesNotThrowAnyException();

    }
}
