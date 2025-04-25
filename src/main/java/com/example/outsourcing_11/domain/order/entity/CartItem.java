package com.example.outsourcing_11.domain.order.entity;

import com.example.outsourcing_11.domain.menu.entity.Menu;
import com.example.outsourcing_11.domain.order.dto.CartItemResponseDto;
import com.example.outsourcing_11.domain.store.entity.Store;
import jakarta.persistence.*;

@Entity
@Table(name = "cart_items")
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id", nullable = false)
    private Cart cart;
//
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id", nullable = false)
    private Menu menu;
//
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    protected CartItem() {
    }

    public CartItem(Cart cart, Menu menu, Store store, int quantity) {
        this.cart = cart;
        this.menu = menu;
        this.store = store;
        this.quantity = quantity;
    }
    public CartItemResponseDto toResponseDto() {
        return new CartItemResponseDto(
                this.id,
                this.menu.getId(),
                this.store.getId(),
                this.quantity
        );
    }

}
