package com.example.outsourcing_11.domain.store.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.outsourcing_11.domain.store.entity.Favorite;
import com.example.outsourcing_11.domain.store.entity.Store;
import com.example.outsourcing_11.domain.user.entity.User;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
	Optional<Favorite> findByUserIdAndStoreId(Long userId, Long storeId);

	boolean existsByUserAndStore(User user, Store store);
}
