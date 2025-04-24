package com.example.outsourcing_11.domain.order.repository;

import com.example.outsourcing_11.domain.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
//
public interface OrderRepository extends JpaRepository<Order, Long> {
}
//