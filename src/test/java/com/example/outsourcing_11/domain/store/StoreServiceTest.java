package com.example.outsourcing_11.domain.store;

import com.example.outsourcing_11.common.UserRole;
import com.example.outsourcing_11.common.exception.CustomException;
import com.example.outsourcing_11.domain.menu.repository.MenuRepository;
import com.example.outsourcing_11.domain.order.repository.OrderRepository;
import com.example.outsourcing_11.domain.store.dto.*;
import com.example.outsourcing_11.domain.store.entity.*;
import com.example.outsourcing_11.domain.store.repository.FavoriteRepository;
import com.example.outsourcing_11.domain.store.repository.NoticeRepository;
import com.example.outsourcing_11.domain.store.repository.StoreRepository;
import com.example.outsourcing_11.domain.store.service.StoreService;
import com.example.outsourcing_11.domain.user.entity.User;
import com.example.outsourcing_11.util.JwtUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class StoreServiceTest {

    @Mock
    private StoreRepository storeRepository;

    @Mock
    private FavoriteRepository favoriteRepository;

    @Mock
    private MenuRepository menuRepository;

    @Mock
    private NoticeRepository noticeRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private StoreService storeService;

    private User createUser(Long id) {
        User user = new User("testUser", "testEmail", "password", "testNumber", "testAddress", UserRole.CUSTOMER);
        try {
            // 리플렉션을 사용하여 ID 설정
            java.lang.reflect.Field idField = User.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(user, id);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("Failed to set user ID", e);
        }
        return user;
    }

    private Store createStore(User owner) {
        Store store = new Store(
            "Test Store",
            "123 Test",
            LocalTime.of(12, 0),
            LocalTime.of(22, 0),
            10000,
            StoreStatus.OPEN,
            StoreCategory.KOREAN,
            owner
        );
        // Store의 notices를 초기화
        try {
            java.lang.reflect.Field noticesField = Store.class.getDeclaredField("notices");
            noticesField.setAccessible(true);
            noticesField.set(store, new ArrayList<Notice>());
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("Failed to initialize store notices", e);
        }
        return store;
    }

    private Favorite createFavorite(User owner, Store store) {
        return new Favorite(owner, store);
    }

    private Notice createNotice(Store store) {
        return new Notice(store, "공지사항 내용");
    }

    private StoreRequestDto createStoreRequestDto() {
        return new StoreRequestDto(
            "Test Store",
            LocalTime.of(12, 0),
            LocalTime.of(22, 0),
            10000,
            "123 Test",
            StoreCategory.KOREAN,
            StoreStatus.OPEN
        );
    }

    @Test
    void 가게_생성_성공() {
        // given
        User user = createUser(1L);
        StoreRequestDto requestDto = createStoreRequestDto();
        Store store = createStore(user);

        given(storeRepository.countByOwnerAndStatus(any(User.class), any(StoreStatus.class))).willReturn(2);
        given(storeRepository.save(any(Store.class))).willReturn(store);

        // when
        StoreResponseDto responseDto = storeService.createStore(user, requestDto);

        // then
        assertThat(responseDto).isNotNull();
        assertThat(responseDto.getName()).isEqualTo(store.getName());
    }

    @Test
    void 가게_생성_실패_3개초과() {
        // given
        User user = createUser(1L);
        StoreRequestDto requestDto = createStoreRequestDto();

        given(storeRepository.countByOwnerAndStatus(any(User.class), any(StoreStatus.class))).willReturn(3);

        // when & then
        assertThrows(CustomException.class, () -> storeService.createStore(user, requestDto));
    }

    @Test
    void 가게_전체조회_성공() {
        // given
        Store store = createStore(createUser(1L));

        given(storeRepository.findAllByCategoryAndDeletedFalse(any())).willReturn(List.of(store));

        // when
        List<StoreListDto> stores = storeService.getStores("KOREAN");

        // then
        assertThat(stores).hasSize(1);
        assertThat(stores.get(0).getName()).isEqualTo(store.getName());
    }

    @Test
    void 가게_조회_잘못된키워드_실패() {
        // when & then
        assertThrows(CustomException.class, () -> storeService.getStores("INVALID"));
    }

    @Test
    void 가게_조회_카테고리에가게없음_실패() {
        // given
        given(storeRepository.findAllByCategoryAndDeletedFalse(any())).willReturn(List.of());

        // when & then
        assertThrows(CustomException.class, () -> storeService.getStores("KOREAN"));
    }

    @Test
    void 가게_상세조회_성공() {
        // given
        User user = createUser(1L);
        Store store = createStore(user);

        given(storeRepository.findByIdAndDeletedFalse(anyLong())).willReturn(Optional.of(store));
        given(menuRepository.findByStoreAndDeletedAtIsNull(any())).willReturn(List.of());

        // when
        StoreDetailDto result = storeService.getStoreDetail(1L);

        // then
        assertThat(result.getName()).isEqualTo(store.getName());
        assertThat(result.getMenus()).isEmpty();
    }

    @Test
    void 가게_상세조회_실패_가게없음() {
        // given
        given(storeRepository.findByIdAndDeletedFalse(anyLong())).willReturn(Optional.empty());

        // when & then
        assertThrows(CustomException.class, () -> storeService.getStoreDetail(1L));
    }

    @Test
    void 내_가게_조회_성공() {
        // given
        User user = createUser(1L);
        Store store = createStore(user);

        given(storeRepository.findAllByOwnerAndDeletedFalse(any())).willReturn(List.of(store));

        // when
        List<StoreResponseDto> stores = storeService.getMyStores(user);

        // then
        assertThat(stores).hasSize(1);
        assertThat(stores.get(0).getName()).isEqualTo(store.getName());
    }

    @Test
    void 내_가게_조회_실패_가게없음() {
        // given
        User user = createUser(1L);
        given(storeRepository.findAllByOwnerAndDeletedFalse(any())).willReturn(List.of());

        // when & then
        assertThrows(CustomException.class, () -> storeService.getMyStores(user));
    }

    @Test
    void 가게_수정_성공() {
        // given
        User user = createUser(1L);
        Store store = createStore(user);
        StoreRequestDto dto = createStoreRequestDto();

        given(storeRepository.findByIdAndOwnerAndDeletedFalse(anyLong(), any())).willReturn(Optional.of(store));

        // when
        storeService.updateStore(1L, dto, user);

        // then
        assertThat(store.getName()).isEqualTo(dto.getName());
    }

    @Test
    void 가게_수정_실패_가게없음() {
        // given
        User user = createUser(1L);
        given(storeRepository.findByIdAndOwnerAndDeletedFalse(anyLong(), any())).willReturn(Optional.empty());

        // when & then
        assertThrows(CustomException.class, () -> storeService.updateStore(1L, createStoreRequestDto(), user));
    }

    @Test
    void 가게_삭제_성공() {
        // given
        User user = createUser(1L);
        Store store = createStore(user);

        given(storeRepository.findByIdAndOwnerAndDeletedFalse(anyLong(), any())).willReturn(Optional.of(store));

        // when
        storeService.deleteStore(1L, user);

        // then
        assertThat(store.isDeleted()).isTrue();
    }

    @Test
    void 가게_삭제_실패_가게없음() {
        // given
        User user = createUser(1L);
        given(storeRepository.findByIdAndOwnerAndDeletedFalse(anyLong(), any())).willReturn(Optional.empty());

        // when & then
        assertThrows(CustomException.class, () -> storeService.deleteStore(1L, user));
    }

    @Test
    void 즐겨찾기_추가_성공() {
        // given
        User user = createUser(1L);
        Store store = createStore(user);

        given(storeRepository.findByIdAndDeletedFalse(anyLong())).willReturn(Optional.of(store));
        given(favoriteRepository.existsByUserAndStore(any(), any())).willReturn(false);
        given(favoriteRepository.save(any())).willReturn(null);

        // when
        storeService.addFavorite(1L, user);

        // then
        assertThat(store.getFavoriteCount()).isEqualTo(1);
    }

    @Test
    void 즐겨찾기_추가_실패_가게없음() {
        // given
        User user = createUser(1L);
        given(storeRepository.findByIdAndDeletedFalse(anyLong())).willReturn(Optional.empty());

        // when & then
        assertThrows(CustomException.class, () -> storeService.addFavorite(1L, user));
    }

    @Test
    void 즐겨찾기_취소_성공() {
        // given
        User user = createUser(1L);
        Store store = createStore(user);
        store.increaseFavorite();
        Favorite favorite = createFavorite(user, store);

        given(storeRepository.findByIdAndDeletedFalse(anyLong())).willReturn(Optional.of(store));
        given(favoriteRepository.findByUserIdAndStoreId(anyLong(), anyLong())).willReturn(Optional.of(favorite));

        // when
        storeService.removeFavorite(1L, user);

        // then
        assertThat(store.getFavoriteCount()).isEqualTo(0);
    }

    @Test
    void 공지_등록_성공() {
        // given
        User user = createUser(1L);
        Store store = createStore(user);
        given(storeRepository.findByIdAndDeletedFalse(anyLong())).willReturn(Optional.of(store));
        given(noticeRepository.save(any(Notice.class))).willReturn(null);

        // when
        storeService.createNotice(1L, "New Notice", user);

        // then
        verify(noticeRepository).save(any(Notice.class));
    }

    @Test
    void 공지_등록_실패_빈공지() {
        // given
        User user = createUser(1L);
        Store store = createStore(user);
        given(storeRepository.findByIdAndDeletedFalse(anyLong())).willReturn(Optional.of(store));

        // when & then
        assertThrows(CustomException.class, () -> storeService.createNotice(1L, " ", user));
    }

    @Test
    void 공지_수정_성공() {
        // given
        User user = createUser(1L);
        Store store = createStore(user);
        Notice notice = createNotice(store);

        given(storeRepository.findByIdAndDeletedFalse(anyLong())).willReturn(Optional.of(store));
        given(noticeRepository.findByIdWithStore(anyLong())).willReturn(Optional.of(notice));

        // when
        storeService.updateNotice(1L, 1L, "Updated Content", user);

        // then
        assertThat(notice.getContent()).isEqualTo("Updated Content");
    }

    @Test
    void 공지_수정_실패_공지없음() {
        // given
        User user = createUser(1L);
        Store store = createStore(user);

        given(storeRepository.findByIdAndDeletedFalse(anyLong())).willReturn(Optional.of(store));
        given(noticeRepository.findByIdWithStore(anyLong())).willReturn(Optional.empty());

        // when & then
        assertThrows(CustomException.class, () -> storeService.updateNotice(1L, 1L, "Content", user));
    }

    @Test
    void 공지_수정_실패_빈공지() {
        // given
        User user = createUser(1L);
        Store store = createStore(user);
        Notice notice = createNotice(store);

        given(storeRepository.findByIdAndDeletedFalse(anyLong())).willReturn(Optional.of(store));
        given(noticeRepository.findByIdWithStore(anyLong())).willReturn(Optional.of(notice));

        // when & then
        assertThrows(CustomException.class, () -> storeService.updateNotice(1L, 1L, " ", user));
    }

    @Test
    void 공지_수정_실패_다른사람가게() {
        // given
        User owner = createUser(1L);
        User anotherUser = createUser(2L);
        Store store = createStore(owner);

        given(storeRepository.findByIdAndDeletedFalse(anyLong())).willReturn(Optional.of(store));

        // when & then
        assertThrows(CustomException.class, () -> storeService.updateNotice(1L, 1L, "Content", anotherUser));
    }

    @Test
    void 공지_삭제_성공() {
        // given
        User user = createUser(1L);
        Store store = createStore(user);
        Notice notice = createNotice(store);

        given(storeRepository.findByIdAndDeletedFalse(anyLong())).willReturn(Optional.of(store));
        given(noticeRepository.findByIdWithStore(anyLong())).willReturn(Optional.of(notice));

        // when
        storeService.deleteNotice(1L, 1L, user);

        // then
        verify(noticeRepository).delete(notice);
    }

    @Test
    void 공지_삭제_실패_공지없음() {
        // given
        User user = createUser(1L);
        Store store = createStore(user);

        given(storeRepository.findByIdAndDeletedFalse(anyLong())).willReturn(Optional.of(store));
        given(noticeRepository.findByIdWithStore(anyLong())).willReturn(Optional.empty());

        // when & then
        assertThrows(CustomException.class, () -> storeService.deleteNotice(1L, 1L, user));
    }

    @Test
    void 판매량조회_성공_daily() {
        // given
        User user = createUser(1L);
        Store store = createStore(user);
        given(storeRepository.findByIdAndOwnerAndDeletedFalse(anyLong(), any())).willReturn(Optional.of(store));
        given(orderRepository.sumTotalPriceByStoreAndCreatedAtBetween(any(), any(), any())).willReturn(BigDecimal.TEN);

        // when
        SalesDto salesDto = storeService.getSales(1L, user, "daily");

        // then
        assertThat(salesDto.getTotalSales()).isEqualTo(BigDecimal.TEN);
    }

    @Test
    void 판매량조회_실패_잘못된타입() {
        // given
        User user = createUser(1L);
        Store store = createStore(user);
        given(storeRepository.findByIdAndOwnerAndDeletedFalse(anyLong(), any())).willReturn(Optional.of(store));

        // when & then
        assertThrows(CustomException.class, () -> storeService.getSales(1L, user, "yearly"));
    }

    @Test
    void 모든가게상태_업데이트_성공() {
        // given
        List<Store> stores = List.of(createStore(createUser(1L)));
        given(storeRepository.findAll()).willReturn(stores);

        // when
        storeService.updateAllStoreStatuses();

        // then
        verify(storeRepository).saveAll(stores);
    }
}
