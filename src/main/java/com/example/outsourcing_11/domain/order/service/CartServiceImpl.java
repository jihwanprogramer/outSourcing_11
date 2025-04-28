package com.example.outsourcing_11.domain.order.service;

import com.example.outsourcing_11.common.exception.CustomException;
import com.example.outsourcing_11.common.exception.ErrorCode;
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
import org.springframework.transaction.annotation.Transactional; // ✅ 수정된 import
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
                .orElseThrow(() -> new CustomException(
                        ErrorCode.CART_EMPTY.getMessage(),
                        ErrorCode.CART_EMPTY.getHttpStatus()
                ));

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
                .orElseThrow(() -> new CustomException(
                        ErrorCode.USER_NOT_FOUND.getMessage(),
                        ErrorCode.USER_NOT_FOUND.getHttpStatus()
                ));


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
        if (dto.getQuantity() <= 0) {
            throw new CustomException(
                    ErrorCode.INVALID_QUANTITY.getMessage(),
                    ErrorCode.INVALID_QUANTITY.getHttpStatus()
            );
        }
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new CustomException(
                        ErrorCode.USER_NOT_FOUND.getMessage(),
                        ErrorCode.USER_NOT_FOUND.getHttpStatus()
                ));


        Cart cart = cartRepository.findByUser(user)
                .orElseGet(() -> cartRepository.save(new Cart(user)));

        Menu menu = menuRepository.findById(dto.getMenuId())
                .orElseThrow(() -> new CustomException(
                        ErrorCode.MENU_NOT_FOUND.getMessage(),
                        ErrorCode.MENU_NOT_FOUND.getHttpStatus()
                ));

        Store store = storeRepository.findById(dto.getStoreId())
                .orElseThrow(() -> new CustomException(
                        ErrorCode.STORE_NOT_FOUND.getMessage(),
                        ErrorCode.STORE_NOT_FOUND.getHttpStatus()
                ));


        CartItem cartItem = new CartItem(cart, menu, store, dto.getQuantity());
        cart.getItems().add(cartItem);
        cartRepository.save(cart);
        // 반환할 응답 객체 생성
        List<CartItemResponseDto> responseItems = cart.getItems().stream()
                .map(CartItem::toResponseDto)
                .toList();
        return new CartResponseDto(cart.getId(), user.getId(), responseItems);
    }

    @Transactional(readOnly = true)
    public Cart getEntityByUserId(Long userId) {
        return cartRepository.findByUserId(userId)
                .orElseThrow(() -> new CustomException(
                        ErrorCode.CART_EMPTY.getMessage(),
                        ErrorCode.CART_EMPTY.getHttpStatus()
                ));
    }

}
