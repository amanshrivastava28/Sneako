package org.genc.sneakoapp.cartmanagementservice.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.genc.sneakoapp.cartmanagementservice.dto.CartDTO;
import org.genc.sneakoapp.cartmanagementservice.dto.CartItemDTO;
import org.genc.sneakoapp.cartmanagementservice.entity.Cart;
import org.genc.sneakoapp.cartmanagementservice.entity.CartItem;
import org.genc.sneakoapp.cartmanagementservice.repo.CartRepository;
import org.genc.sneakoapp.cartmanagementservice.service.api.CartService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;

    @Override
    @Transactional
    public CartDTO getCartByUserId(Long userId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseGet(() -> {
                    Cart newCart = Cart.builder()
                            .userId(userId)
                            .totalPrice(BigDecimal.ZERO)
                            .totalItem(0)
                            .build();
                    return cartRepository.save(newCart);
                });

        Set<CartItemDTO> itemDTOs = cart.getCartItems().stream()
                .map(item -> CartItemDTO.builder()
                        .cartItemId(item.getCartItemId())
                        .productId(item.getProductId())
                        .unitPrice(item.getUnitPrice().doubleValue())
                        .quantity(item.getQuantity())
                        .totalPrice(item.getTotalPrice().doubleValue())
                        .size(item.getSize()) // Added size mapping
                        .build())
                .collect(Collectors.toSet());

        return new CartDTO(
                cart.getId(),
                cart.getUserId(),
                itemDTOs,
                cart.getTotalPrice().doubleValue(),
                cart.getTotalItem()
        );
    }

    @Override
    @Transactional
    public CartDTO recalculateCart(Long cartId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        BigDecimal totalPrice = cart.getCartItems().stream()
                .map(CartItem::getTotalPrice) // ✅ Already BigDecimal
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        int totalItems = cart.getCartItems().size();

        cart.setTotalPrice(totalPrice);
        cart.setTotalItem(totalItems);

        cartRepository.save(cart);

        return getCartByUserId(cart.getUserId());
    }
}
