package com.example.outsourcing_11.domain.order.entity;

import com.example.outsourcing_11.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "order_date", nullable = false)
    private LocalDateTime orderDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private OrderStatus status = OrderStatus.PENDING;

    @Column(name = "total_price", nullable = false)
    private int totalPrice;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items = new ArrayList<>();

    public Order(User user, LocalDateTime orderDate, OrderStatus status, int totalPrice, List<OrderItem> items) {
        this.user = user;
        this.orderDate = orderDate;
        this.status = status;
        this.totalPrice = totalPrice;
        this.items = items;

        // 양방향 연관관계 설정
        for (OrderItem item : items) {
            item.setOrder(this);
        }
    }


}
