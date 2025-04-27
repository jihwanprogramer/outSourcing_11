package com.example.outsourcing_11.domain.order.repository;

import com.example.outsourcing_11.domain.order.entity.Cart;
import com.example.outsourcing_11.domain.order.entity.Order;
import com.example.outsourcing_11.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByUserId(Long userId);

    Optional<Cart> findByUser(User user);

    void deleteAllByUser(User user);
}
