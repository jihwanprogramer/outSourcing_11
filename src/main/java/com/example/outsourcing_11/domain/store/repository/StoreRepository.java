package com.example.outsourcing_11.domain.store.repository;

import com.example.outsourcing_11.domain.store.entity.Store;
import com.example.outsourcing_11.domain.store.entity.StoreStatus;
import com.example.outsourcing_11.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StoreRepository extends JpaRepository<Store, Long> {

    default Store finByIdOrElseThrow(Long storeId) {
        return findById(storeId).orElseThrow(() -> new NullPointerException("존재하지 않는 가게입니다"));
    }

    List<Store> findByNameContainingAndStatus(String name, StoreStatus status);

    List<Store> findByOwnerAndStatus(User owner, StoreStatus status);

    int countByOwnerAndStatus(User owner, StoreStatus status);

}
