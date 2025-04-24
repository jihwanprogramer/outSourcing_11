package com.example.outsourcing_11.domain.menu.entity;

import com.example.outsourcing_11.common.Base;
import com.example.outsourcing_11.common.Status;
import com.example.outsourcing_11.domain.menu.dto.request.MenuUpdateRequestDto;
import com.example.outsourcing_11.domain.menu.enums.Category;
import com.example.outsourcing_11.domain.menu.enums.MenuStatus;
import com.example.outsourcing_11.domain.store.entity.Store;
import jakarta.persistence.*;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Entity
public class Menu extends Base {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.ORDINAL)
    @Column(nullable = false)
    private Category category;

    @Column(nullable = false)
    private String name;

    @Column
    private String content;

    @Column(nullable = false)
    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MenuStatus status;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "storeId")
    private Store store;

    @Column(columnDefinition = "TINYINT(1) DEFAULT 1")
    private boolean isDeleted = Status.EXIST.getValue();

    public void updateDeleteStatus(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Menu() {

    }

    //정적 생성자 (캡슐화)
    public static Menu of(Category category, String name, String content, BigDecimal price, MenuStatus status, Store store) {
        Menu menu = new Menu(category, name, content, price, status, store);

        return menu;
    }

    public Menu(Category category, String name, String content, BigDecimal price, MenuStatus status, Store store) {
        this.category = category;
        this.name = name;
        this.content = content;
        this.price = price;
        this.status = status;
        this.store = store;
    }

    public void update(MenuUpdateRequestDto dto) {
        if (dto.getCategory() != null) this.category = dto.getCategory();
        if (dto.getMenuName() != null) this.name = dto.getMenuName();
        if (dto.getContent() != null) this.content = dto.getContent();
        if (dto.getPrice() != null) this.price = dto.getPrice();
        if (dto.getStatus() != null) this.status = dto.getStatus();
    }

}
