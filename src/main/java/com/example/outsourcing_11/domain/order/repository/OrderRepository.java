package com.example.outsourcing_11.domain.order.repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.outsourcing_11.domain.order.entity.Order;
import com.example.outsourcing_11.domain.store.entity.Store;

//
@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
	List<Order> findByUserId(Long userId);

	BigDecimal sumTotalPriceByStoreAndCreatedAtBetween(Store store, LocalDateTime start, LocalDateTime now);
}
//
