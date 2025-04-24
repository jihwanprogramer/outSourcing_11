package com.example.outsourcing_11.domain.store.service;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import com.example.outsourcing_11.common.exception.store.StoreCustomException;
import com.example.outsourcing_11.common.exception.store.StoreErrorCode;
import com.example.outsourcing_11.domain.menu.repository.MenuRepository;
import com.example.outsourcing_11.domain.store.repository.StoreRepository;
import com.example.outsourcing_11.domain.user.entity.User;
import com.example.outsourcing_11.domain.user.repository.UserRepository;
import com.example.outsourcing_11.util.JwtUtil;

@Service
@RequiredArgsConstructor
public class StoreService {

	private final StoreRepository storeRepository;
	private final UserRepository userRepository;
	private final MenuRepository menuRepository;
	private final JwtUtil jwtUtil;

	/**
	 *
	 * 현재 유저 반환 메서드
	 */
	private User getCurrentUser(String token) {
		Long userId = jwtUtil.extractUserId(token);
		return userRepository.findById(userId)
			.orElseThrow(() -> new StoreCustomException(StoreErrorCode.USER_NOT_FOUND));
	}

	private void validateOwnerRole(String token) {
		String role = jwtUtil.extractRoleFromToken(token);
		if (!role.equals("사장님")) {
			throw new StoreCustomException(StoreErrorCode.ONLY_OWNER);
		}
	}
	//
	// public ResponseEntity<StoreResponseDto> createStore(String token, StoreRequestDto requestDto) {
	//
	// 	User user = getCurrentUser(token);
	// 	validateOwnerRole(token);
	// 	int storeCount = storeRepository.countByOwnerAndStatus(user, StoreStatus.OPEN);
	// 	if (storeCount >= 3) {
	// 		throw new StoreCustomException(StoreErrorCode.LIMIT_THREE);
	// 	}
	//
	// 	Store store = new Store(requestDto, user);
	//
	// }

}
