package com.example.outsourcing_11.domain.store.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.outsourcing_11.domain.store.entity.Store;
import com.example.outsourcing_11.domain.store.entity.StoreStatus;
import com.example.outsourcing_11.domain.user.entity.User;

public interface StoreRepository extends JpaRepository<Store, Long> {

	List<Store> findByNameContainingAndStatus(String name, StoreStatus status);

	List<Store> findByOwnerAndStatus(User owner, StoreStatus status);

	int countByOwnerAndStatus(User owner, StoreStatus status);

}
