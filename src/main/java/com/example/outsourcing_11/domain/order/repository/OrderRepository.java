package com.example.outsourcing_11.domain.order.repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.example.outsourcing_11.domain.order.entity.Order;
import com.example.outsourcing_11.domain.store.entity.Store;

//
@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
	List<Order> findByUserId(Long userId);

	@Query("SELECT SUM(o.totalPrice) FROM Order o WHERE o.store = :store AND o.orderDate BETWEEN :start AND :now")
	BigDecimal sumTotalPriceByStoreAndCreatedAtBetween(
		@Param("store") Store store,
		@Param("start") LocalDateTime start,
		@Param("now") LocalDateTime now

	);
}
//
