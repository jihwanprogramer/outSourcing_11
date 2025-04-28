package com.example.outsourcing_11.domain.store.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.outsourcing_11.common.exception.CustomException;
import com.example.outsourcing_11.common.exception.ErrorCode;
import com.example.outsourcing_11.domain.menu.dto.response.MenuUserResponseDto;
import com.example.outsourcing_11.domain.menu.entity.Menu;
import com.example.outsourcing_11.domain.menu.repository.MenuRepository;
import com.example.outsourcing_11.domain.order.repository.OrderRepository;
import com.example.outsourcing_11.domain.store.dto.SalesDto;
import com.example.outsourcing_11.domain.store.dto.StoreDetailDto;
import com.example.outsourcing_11.domain.store.dto.StoreListDto;
import com.example.outsourcing_11.domain.store.dto.StoreRequestDto;
import com.example.outsourcing_11.domain.store.dto.StoreResponseDto;
import com.example.outsourcing_11.domain.store.entity.Favorite;
import com.example.outsourcing_11.domain.store.entity.Notice;
import com.example.outsourcing_11.domain.store.entity.Store;
import com.example.outsourcing_11.domain.store.entity.StoreCategory;
import com.example.outsourcing_11.domain.store.entity.StoreStatus;
import com.example.outsourcing_11.domain.store.repository.FavoriteRepository;
import com.example.outsourcing_11.domain.store.repository.NoticeRepository;
import com.example.outsourcing_11.domain.store.repository.StoreRepository;
import com.example.outsourcing_11.domain.user.entity.User;

@Service
@RequiredArgsConstructor
public class StoreService {

	private final StoreRepository storeRepository;
	private final MenuRepository menuRepository;
	private final FavoriteRepository favoriteRepository;
	private final NoticeRepository noticeRepository;
	private final OrderRepository orderRepository;

	private void validateOwner(Store store, User user) {
		if (!store.getOwner().getId().equals(user.getId())) {
			throw new CustomException(ErrorCode.ONLY_MY_STORE); // "내 가게 아님" 오류
		}
	}

	private Store getStoreOrThrow(Long storeId) {
		return storeRepository.findByIdAndDeletedFalse(storeId)
			.orElseThrow(() -> new CustomException(ErrorCode.STORE_NOT_FOUND));
	}

	/**
	 * 가게 생성 3개까지 제한
	 */
	public StoreResponseDto createStore(User user, StoreRequestDto requestDto) {
		int storeCount = storeRepository.countByOwnerAndStatus(user, StoreStatus.OPEN);
		if (storeCount >= 3) {
			throw new CustomException(ErrorCode.LIMIT_THREE);
		}
		if (requestDto.getCategory() == null) {
			throw new CustomException(ErrorCode.KEYWORD_NOT_FOUND);
		}

		Store store = new Store(requestDto, user);

		return new StoreResponseDto(storeRepository.save(store));

	}

	/**
	 * 고객 다건 조회
	 */
	@Transactional(readOnly = true)
	public List<StoreListDto> getStores(String keyword) {
		List<Store> stores;

		if (keyword == null) {
			stores = storeRepository.findAllByDeletedFalse();
		} else {
			try {
				StoreCategory category = StoreCategory.valueOf(keyword);
				stores = storeRepository.findAllByCategoryAndDeletedFalse(category);

				if (stores.isEmpty()) {
					throw new CustomException(ErrorCode.NO_STORE_IN_CATEGORY);
				}

			} catch (IllegalArgumentException e) {
				throw new CustomException(ErrorCode.KEYWORD_NOT_FOUND);
			}
		}
		return stores.stream()
			.map(StoreListDto::new)
			.collect(Collectors.toList());
	}

	/**
	 * 고객 단건 조회 - 메뉴리스트 포함
	 *
	 * @param storeId
	 * @return
	 */
	@Transactional(readOnly = true)
	public StoreDetailDto getStoreDetail(Long storeId) {
		Store store = getStoreOrThrow(storeId);

		List<Menu> menus = menuRepository.findByStoreAndDeletedAtIsNull(store);

		List<MenuUserResponseDto> menuDtos = menus.stream()
			.map(menu -> new MenuUserResponseDto(menu, 0))  // commentCount는 일단 0으로
			.toList();

		return new StoreDetailDto(store, menuDtos);
	}

	/**
	 * 내 가게 조회
	 *
	 * @return
	 */
	@Transactional(readOnly = true)
	public List<StoreResponseDto> getMyStores(User user) {
		List<Store> stores = storeRepository.findAllByOwnerAndDeletedFalse(user);

		if (stores.isEmpty()) {
			throw new CustomException(ErrorCode.MY_STORE_NOT_FOUND); // 새로 추가
		}

		return stores
			.stream()
			.map(StoreResponseDto::new)
			.collect(Collectors.toList());
	}

	/**
	 * 가게 수정
	 */
	@Transactional
	public void updateStore(Long storeId, StoreRequestDto dto, User user) {

		Store store = storeRepository.findByIdAndOwnerAndDeletedFalse(storeId, user)
			.orElseThrow(() -> new CustomException(ErrorCode.STORE_NOT_FOUND));
		validateOwner(store, user);
		store.update(dto);

	}

	/**
	 * 가게 삭제 - soft delete
	 *
	 * @param storeId
	 * @param user
	 */
	@Transactional
	public void deleteStore(Long storeId, User user) {
		//사장님인지 아닌지 물어봄. 그러고 통과하면 그때 storeId로 묻고 , 삭제여부도 따로 묻기
		Store store = storeRepository.findByIdAndOwnerAndDeletedFalse(storeId, user)
			.orElseThrow(() -> new CustomException(ErrorCode.STORE_NOT_FOUND));

		validateOwner(store, user);
		store.softDelete();
	}

	/**
	 * 즐겨찾기 등록
	 *
	 * @param storeId
	 * @param user
	 */
	public void addFavorite(Long storeId, User user) {
		Store store = getStoreOrThrow(storeId);
		boolean alreadyFavorite = favoriteRepository.existsByUserAndStore(user, store);
		if (alreadyFavorite) {
			throw new CustomException(ErrorCode.ALREADY_FAVORITE);
		}

		store.increaseFavorite();

		favoriteRepository.save(new Favorite(user, store));
	}

	/**
	 * 즐겨찾기 해제
	 *
	 * @param storeId
	 * @param user
	 */
	@Transactional
	public void removeFavorite(Long storeId, User user) {

		Store store = getStoreOrThrow(storeId);

		Favorite favorite = favoriteRepository.findByUserIdAndStoreId(user.getId(), storeId)
			.orElseThrow(() -> new CustomException(ErrorCode.NON_FAVORITE));
		favoriteRepository.delete(favorite);

		store.decreaseFavorite();
	}

	/**
	 * 공지 등록
	 *
	 * @param storeId
	 * @param content
	 * @param user
	 */

	public void createNotice(Long storeId, String content, User user) {
		Store store = getStoreOrThrow(storeId);

		validateOwner(store, user);

		if (content == null || content.trim().isEmpty()) {
			throw new CustomException(ErrorCode.NON_CONTENT);
		}

		noticeRepository.save(new Notice(store, content));
	}

	/**
	 * 공지 수정
	 *
	 * @param noticeId
	 * @param content
	 * @param user
	 */
	@Transactional
	public void updateNotice(Long storeId, Long noticeId, String content, User user) {

		Store store = getStoreOrThrow(storeId);
		validateOwner(store, user);

		Notice notice = noticeRepository.findByIdWithStore(noticeId)
			.orElseThrow(() -> new CustomException(ErrorCode.NO_NOTICE));

		if (content == null || content.trim().isEmpty()) {
			throw new CustomException(ErrorCode.NON_CONTENT);
		}

		if (!notice.getStore().getOwner().getId().equals(user.getId())) {
			throw new CustomException(ErrorCode.ONLY_MY_STORE);
		}

		notice.update(content);
	}

	/**
	 * 공지 삭제
	 *
	 * @param noticeId
	 * @param user
	 */
	@Transactional
	public void deleteNotice(Long storeId, Long noticeId, User user) {

		Store store = getStoreOrThrow(storeId);
		validateOwner(store, user);

		Notice notice = noticeRepository.findByIdWithStore(noticeId)
			.orElseThrow(() -> new CustomException(ErrorCode.NO_NOTICE));

		noticeRepository.delete(notice);
	}

	/**
	 * 판매량 통계
	 *
	 * @param storeId
	 * @param user
	 * @param type
	 * @return
	 */
	@Transactional(readOnly = true)
	public SalesDto getSales(Long storeId, User user, String type) {
		Store store = storeRepository.findByIdAndOwnerAndDeletedFalse(storeId, user)
			.orElseThrow(() -> new CustomException(ErrorCode.STORE_NOT_FOUND));

		LocalDateTime now = LocalDateTime.now();
		LocalDateTime start;

		switch (type.toLowerCase()) {
			case "daily":
				start = now.toLocalDate().atStartOfDay();
				break;
			case "weekly":
				start = now.minusWeeks(1);
				break;
			case "monthly":
				start = now.minusMonths(1);
				break;

			default:
				throw new CustomException(ErrorCode.PERIOD_ERROR);
		}

		BigDecimal totalSales = orderRepository.sumTotalPriceByStoreAndCreatedAtBetween(store, start, now);
		if (totalSales == null) {
			totalSales = BigDecimal.ZERO;
		}
		return new SalesDto(totalSales);

	}

	/**
	 * 자동으로 상태 영업시작, 마감
	 */
	@Transactional
	public void updateAllStoreStatuses() {
		System.out.println("스케줄러 돈다! " + LocalDateTime.now());
		List<Store> stores = storeRepository.findAll();
		for (Store store : stores) {
			store.updateStatus();
		}
		storeRepository.saveAll(stores);
	}
}
