package com.example.outsourcing_11.domain.order.service;

import com.example.outsourcing_11.domain.menu.entity.Menu;
import com.example.outsourcing_11.domain.menu.repository.MenuRepository;
import com.example.outsourcing_11.domain.order.dto.CartItemRequestDto;
import com.example.outsourcing_11.domain.order.dto.CartItemResponseDto;
import com.example.outsourcing_11.domain.order.dto.CartRequestDto;
import com.example.outsourcing_11.domain.order.dto.CartResponseDto;
import com.example.outsourcing_11.domain.order.entity.Cart;
import com.example.outsourcing_11.domain.order.entity.CartItem;
import com.example.outsourcing_11.domain.order.repository.CartRepository;
import com.example.outsourcing_11.domain.store.entity.Store;
import com.example.outsourcing_11.domain.store.repository.StoreRepository;
import com.example.outsourcing_11.domain.user.entity.User;
import com.example.outsourcing_11.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final MenuRepository menuRepository;
    private final StoreRepository storeRepository;

    /**
     * 사용자 ID를 기반으로 장바구니(Cart)를 조회하여 CartResponseDto로 반환
     * 장바구니가 존재하지 않을 경우 예외를 발생시킨다.
     * 현재는 장바구니 항목(CartItem)은 비어 있는 리스트로 응답한다.
     *
     * @param userId 사용자 ID
     * @return CartResponseDto (장바구니 ID, 사용자 ID, 장바구니 항목 리스트)
     */

    @Override
    public CartResponseDto getCartByUserId(Long userId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        return CartResponseDto.builder()
                .id(cart.getId())
                .userId(cart.getUser().getId())
                .items(Collections.emptyList()) // 실제 CartItemResponseDto 리스트 매핑 필요
                .build();
    }

    /**
     * 요청된 사용자 ID를 기반으로 새로운 장바구니(Cart)를 생성하고 저장한다.
     * 저장된 장바구니 정보를 CartResponseDto 형태로 반환한다.
     * 현재는 장바구니 항목(CartItem)은 비어 있는 리스트로 응답한다.
     *
     * @param requestDto 사용자 ID를 포함한 CartRequestDto
     * @return CartResponseDto (생성된 장바구니 정보)
     */
    @Override
    public CartResponseDto createCart(CartRequestDto requestDto) {
        User user = userRepository.findById(requestDto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // ✅ 이미 Cart가 존재하는지 확인
        if (cartRepository.findByUser(user).isPresent()) {
            throw new RuntimeException("이미 장바구니가 존재합니다.");  // 또는 CustomException 사용
        }

        Cart cart = new Cart(user);

        Cart saved = cartRepository.save(cart);

        return CartResponseDto.builder()
                .id(saved.getId())
                .userId(saved.getUser().getId())
                .items(Collections.emptyList()) // CartItemResponseDto 리스트 매핑 필요
                .build();
    }

    @Override
    public CartResponseDto addItemToCart(CartItemRequestDto dto) {
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자를 찾을 수 없습니다."));

        Cart cart = cartRepository.findByUser(user).orElseGet(() -> cartRepository.save(new Cart(user)));

        Menu menu = menuRepository.findById(dto.getMenuId())
                .orElseThrow(() -> new IllegalArgumentException("해당 메뉴를 찾을 수 없습니다."));

        Store store = storeRepository.findById(dto.getStoreId())
                .orElseThrow(() -> new RuntimeException("해당 가게가 존재하지 않습니다."));

        CartItem cartItem = new CartItem(cart, menu, store, dto.getQuantity());
        cart.getItems().add(cartItem);
        cartRepository.save(cart);  // cascade로 cartItem도 저장됨

        // 반환할 응답 객체 생성
        List<CartItemResponseDto> responseItems = cart.getItems().stream()
                .map(CartItem::toResponseDto)
                .toList();
        return new CartResponseDto(cart.getId(), user.getId(), responseItems);
    }

    @Transactional(readOnly = true)
    public Cart getEntityByUserId(Long userId) {
        return cartRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("장바구니가 존재하지 않습니다."));
    }

}
