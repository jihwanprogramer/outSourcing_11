package com.example.outsourcing_11.domain.store.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.outsourcing_11.domain.store.entity.Store;
import com.example.outsourcing_11.domain.store.entity.StoreCategory;
import com.example.outsourcing_11.domain.store.entity.StoreStatus;
import com.example.outsourcing_11.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {

	List<Store> findByCategoryAndStatus(StoreCategory category, StoreStatus status);
    default Store finByIdOrElseThrow(Long storeId) {
        return findById(storeId).orElseThrow(() -> new NullPointerException("존재하지 않는 가게입니다"));
    }

    List<Store> findByNameContainingAndStatus(String name, StoreStatus status);

	List<Store> findByOwnerAndDeletedAtnull(User owner, StoreStatus status);
    List<Store> findByOwnerAndStatus(User owner, StoreStatus status);

	int countByOwnerAndStatus(User owner, StoreStatus status);

}
