package com.example.outsourcing_11.domain.menu.service;

import com.example.outsourcing_11.domain.comment.repository.CommentRepository;
import com.example.outsourcing_11.domain.menu.dto.response.MenuUserResponseDto;
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
    private final CommentRepository commentRepository;

    @Override
    @Transactional
    public Slice<MenuUserResponseDto> findCursorMenuBySize(Long storeId, Category categoryCursor, Long lastId, int size) {
        Pageable pageable = PageRequest.of(0, size);
        Slice<Menu> menuSlice = menuRepository.findByCursorMenuBySize(storeId, categoryCursor, lastId, pageable);

        return menuSlice.map(menu -> {
            long commentCount = commentRepository.countByMenuId((menu.getId()));
            return new MenuUserResponseDto(menu, commentCount);
        });
    }

}



