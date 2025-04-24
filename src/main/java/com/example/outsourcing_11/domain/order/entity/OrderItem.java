package com.example.outsourcing_11.domain.order.entity;

import com.example.outsourcing_11.domain.menu.entity.Menu;
import com.example.outsourcing_11.domain.store.entity.Store;
import jakarta.persistence.*;


@Entity
@Table(name = "order_items")
public class OrderItem {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id", nullable = false)
    private Menu menu;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    @Column(name = "item_price", nullable = false)
    private int itemPrice;

    public void setOrder(Order order) {
        this.order = order;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setItemPrice(int itemPrice) {
        this.itemPrice = itemPrice;
    }
    public Order getOrder() {
        return order;
    }

    public Menu getMenu() {
        return menu;
    }

    public Store getStore() {
        return store;
    }

    public int getQuantity() {
        return quantity;
    }
//
    public int getItemPrice() {
        return itemPrice;
    }
    public Long getId() {
        return id;
    }
    //
}
