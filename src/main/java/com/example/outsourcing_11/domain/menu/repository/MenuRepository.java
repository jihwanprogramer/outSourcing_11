package com.example.outsourcing_11.domain.menu.repository;

import com.example.outsourcing_11.common.exception.CustomException;
import com.example.outsourcing_11.common.exception.ErrorCode;
import com.example.outsourcing_11.domain.menu.entity.Menu;
import com.example.outsourcing_11.domain.menu.enums.Category;
import com.example.outsourcing_11.domain.store.entity.Store;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuRepository extends JpaRepository<Menu, Long> {
    default Menu finByIdOrElseThrow(Long menuId) {
        return findById(menuId).orElseThrow(() -> new CustomException(ErrorCode.MENU_NOT_FOUND));
    }

    @Query("""
            SELECT m FROM Menu m
            WHERE m.store.id = :storeId
            AND (:categoryCursor IS NULL
                   OR m.category > :categoryCursor
                   OR (m.category = :categoryCursor AND m.id > :lastId)
                  )
            AND m.deletedAt IS NULL
            ORDER BY m.category ASC, m.id ASC
        """)
    Slice<Menu> findByCursorMenuBySize(@Param("storeId") Long storeId,
                                       @Param("categoryCursor") Category categoryCursor,
                                       @Param("lastId") Long lastId,
                                       Pageable pageable);

    List<Menu> findByStoreAndDeletedAtIsNull(Store store);
}

