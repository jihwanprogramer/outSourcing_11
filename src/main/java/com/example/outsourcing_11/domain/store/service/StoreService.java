package com.example.outsourcing_11.domain.store.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.outsourcing_11.common.exception.store.StoreCustomException;
import com.example.outsourcing_11.common.exception.store.StoreErrorCode;
import com.example.outsourcing_11.domain.order.repository.OrderRepository;
import com.example.outsourcing_11.domain.store.dto.MenuDto;
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
import com.example.outsourcing_11.domain.user.repository.UserRepository;
import com.example.outsourcing_11.util.JwtUtil;

@Service
@RequiredArgsConstructor
public class StoreService {

	private final StoreRepository storeRepository;
	private final UserRepository userRepository;
	private final FavoriteRepository favoriteRepository;
	private final NoticeRepository noticeRepository;
	private final OrderRepository orderRepository;
	private final JwtUtil jwtUtil;

	/**
	 * 가게 생성 3개까지 제한
	 *
	 */
	public StoreResponseDto createStore(User user, StoreRequestDto requestDto) {
		int storeCount = storeRepository.countByOwnerAndStatus(user, StoreStatus.OPEN);
		if (storeCount >= 3) {
			throw new StoreCustomException(StoreErrorCode.LIMIT_THREE);
		}

		Store store = new Store(requestDto, user);

		return new StoreResponseDto(storeRepository.save(store));

	}

	/**
	 *
	 * 고객 다건 조회
	 *
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
			} catch (IllegalArgumentException e) {
				throw new StoreCustomException(StoreErrorCode.KEYWORD_NOT_FOUND);
			}
		}
		return stores.stream()
			.map(StoreListDto::new)
			.collect(Collectors.toList());
	}

	/**
	 * 고객 단건 조회 - 메뉴리스트 포함
	 * @param storeId
	 * @return
	 */
	@Transactional(readOnly = true)
	public StoreDetailDto getStoreDetail(Long storeId) {
		Store store = storeRepository.findByIdAndDeletedFalse(storeId)
			.orElseThrow(() -> new StoreCustomException(StoreErrorCode.STORE_NOT_FOUND));

		List<MenuDto> menus = store.getMenus().stream()
			.filter(menu -> !menu.isDeleted())
			.map(MenuDto::new)
			.collect(Collectors.toList());
		return new StoreDetailDto(store, menus);
	}

	/**
	 * 내 가게 조회
	 *
	 * @return
	 */
	@Transactional(readOnly = true)
	public List<StoreResponseDto> getMyStores(User user) {
		return storeRepository.findAllByOwnerAndDeletedFalse(user)
			.stream()
			.map(StoreResponseDto::new)
			.collect(Collectors.toList());
	}

	/**
	 * 가게 수정
	 * @param storeId
	 * @param requestDto
	 * @param user
	 */
	public void updateStore(Long storeId, StoreRequestDto requestDto, User user) {

		Store store = storeRepository.findByIdAndOwnerAndDeletedFalse(storeId, user)
			.orElseThrow(() -> new StoreCustomException(StoreErrorCode.STORE_NOT_FOUND));

		store.update(requestDto);

	}

	/**
	 * 가게 삭제 - soft delete
	 * @param storeId
	 * @param user
	 */
	public void deleteStore(Long storeId, User user) {
		Store store = storeRepository.findByIdAndOwnerAndDeletedFalse(storeId, user)
			.orElseThrow(() -> new StoreCustomException(StoreErrorCode.STORE_NOT_FOUND));
		store.softDelete();
	}

	/**
	 * 즐겨찾기 등록
	 * @param storeId
	 * @param user
	 */
	public void addFavorite(Long storeId, User user) {
		Store store = storeRepository.findByIdAndDeletedFalse(storeId)
			.orElseThrow(() -> new StoreCustomException(StoreErrorCode.STORE_NOT_FOUND));

		boolean alreadyFavorite = favoriteRepository.existsByUserAndStore(user, store);
		if (alreadyFavorite) {
			throw new StoreCustomException(StoreErrorCode.ALREADY_FAVORITE);
		}

		favoriteRepository.save(new Favorite(user, store));
	}

	/**
	 * 즐겨찾기 해제
	 * @param storeId
	 * @param user
	 */
	public void removeFavorite(Long storeId, User user) {
		Favorite favorite = favoriteRepository.findByUserIdAndStoreId(user.getId(), storeId)
			.orElseThrow(() -> new StoreCustomException(StoreErrorCode.NON_FAVORITE));

		favoriteRepository.delete(favorite);
	}

	/**
	 * 공지 등록
	 * @param storeId
	 * @param content
	 * @param user
	 */
	public void createNotice(Long storeId, String content, User user) {
		Store store = storeRepository.findByIdAndOwnerAndDeletedFalse(storeId, user)
			.orElseThrow(() -> new StoreCustomException(StoreErrorCode.STORE_NOT_FOUND));

		if (content == null || content.trim().isEmpty()) {
			throw new StoreCustomException(StoreErrorCode.NON_CONTENT);
		}

		noticeRepository.save(new Notice(store, content));
	}

	/**
	 * 공지 수정
	 * @param noticeId
	 * @param content
	 * @param user
	 */
	public void updateNotice(Long noticeId, String content, User user) {

		Notice notice = noticeRepository.findByIdWithStore(noticeId)
			.orElseThrow(() -> new StoreCustomException(StoreErrorCode.NO_NOTICE));

		if (content == null || content.trim().isEmpty()) {
			throw new StoreCustomException(StoreErrorCode.NON_CONTENT);
		}

		if (!notice.getStore().getOwner().getId().equals(user.getId())) {
			throw new StoreCustomException(StoreErrorCode.ONLY_MY_STORE);
		}

		notice.update(content);
	}

	/**
	 * 공지 삭제
	 * @param noticeId
	 * @param user
	 */
	public void deleteNotice(Long noticeId, User user) {
		Notice notice = noticeRepository.findByIdWithStore(noticeId)
			.orElseThrow(() -> new StoreCustomException(StoreErrorCode.NO_NOTICE));

		if (!notice.getStore().getOwner().getId().equals(user.getId())) {
			throw new StoreCustomException(StoreErrorCode.ONLY_MY_STORE);
		}

		noticeRepository.delete(notice);
	}

	/**
	 * 판매량 통계
	 * @param storeId
	 * @param user
	 * @param type
	 * @return
	 */
	@Transactional(readOnly = true)
	public SalesDto getSales(Long storeId, User user, String type) {
		Store store = storeRepository.findByIdAndOwnerAndDeletedFalse(storeId, user)
			.orElseThrow(() -> new StoreCustomException(StoreErrorCode.STORE_NOT_FOUND));

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
				throw new StoreCustomException(StoreErrorCode.PERIOD_ERROR);
		}

		BigDecimal totalSales = orderRepository.sumTotalPriceByStoreAndCreatedAtBetween(store, start, now);
		return new SalesDto(totalSales);

	}

	/**
	 * 자동으로 상태 영업시작, 마감
	 */
	@Transactional
	public void updateAllStoreStatuses() {
		List<Store> stores = storeRepository.findAll();
		for (Store store : stores) {
			store.updateStatus();
		}
		storeRepository.saveAll(stores);
	}
}
