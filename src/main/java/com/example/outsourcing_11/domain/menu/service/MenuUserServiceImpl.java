package com.example.outsourcing_11.domain.menu.service;

import com.example.outsourcing_11.domain.menu.entity.Menu;
import com.example.outsourcing_11.domain.menu.enums.Category;
import com.example.outsourcing_11.domain.menu.repository.MenuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MenuUserServiceImpl implements MenuUserService {

    private final MenuRepository menuRepository;

    @Override
    @Transactional
    public Slice<Menu> findCursorMenuBySize(Category categoryCursor, Long lastId, int size) {
        Pageable pageable = PageRequest.of(0, size);
        return menuRepository.findByCursorMenuBySize(categoryCursor, lastId, pageable);

    }
}
