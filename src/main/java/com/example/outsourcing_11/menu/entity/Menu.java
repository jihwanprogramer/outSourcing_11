package com.example.outsourcing_11.menu.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String category;

    @Column(nullable = false)
    private String name;

    @Column
    private String content;

    @Column(nullable = false)
    private Integer price;

    @Column(nullable = false)
    private String status;

}
