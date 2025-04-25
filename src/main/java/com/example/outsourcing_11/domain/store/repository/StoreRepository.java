package com.example.outsourcing_11.domain.store.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.outsourcing_11.domain.store.dto.StoreResponseDto;
import com.example.outsourcing_11.domain.store.entity.Store;
import com.example.outsourcing_11.domain.store.entity.StoreCategory;
import com.example.outsourcing_11.domain.store.entity.StoreStatus;
import com.example.outsourcing_11.domain.user.entity.User;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {

	default Store finByIdOrElseThrow(Long storeId) {
		return findById(storeId).orElseThrow(() -> new NullPointerException("존재하지 않는 가게입니다"));
	}

	int countByOwnerAndStatus(User owner, StoreStatus status);

	List<Store> findAllByDeletedFalse();

	List<Store> findAllByCategoryAndDeletedFalse(StoreCategory storeCategory);

	Optional<Store> findByIdAndDeletedFalse(Long storeId);

	List<StoreResponseDto> findAllByOwnerAndDeletedFalse(User user);

	Optional<Store> findByIdAndOwnerAndDeletedFalse(Long storeId, User user);
}
