package org.genc.sneakoapp.cartmanagementservice.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.genc.sneakoapp.cartmanagementservice.dto.CartItemDTO;
import org.genc.sneakoapp.cartmanagementservice.entity.Cart;
import org.genc.sneakoapp.cartmanagementservice.entity.CartItem;
import org.genc.sneakoapp.cartmanagementservice.repo.CartItemRepository;
import org.genc.sneakoapp.cartmanagementservice.repo.CartRepository;
import org.genc.sneakoapp.cartmanagementservice.service.api.CartItemService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class CartItemServiceImpl implements CartItemService {

    private final CartItemRepository cartItemRepository;
    private final CartRepository cartRepository;

    @Override
    @Transactional
    public CartItemDTO createCartItem(CartItemDTO cartItemDTO) {
        Cart cart = cartRepository.findByUserId(cartItemDTO.getUserId())
                .orElseGet(() -> {
                    Cart newCart = Cart.builder()
                            .userId(cartItemDTO.getUserId())
                            .totalPrice(BigDecimal.ZERO)
                            .totalItem(0)
                            .cartItems(new HashSet<>())
                            .build();
                    return cartRepository.save(newCart);
                });

        List<CartItem> existingItems = cartItemRepository.findExistingItems(
                cartItemDTO.getUserId(),
                cartItemDTO.getProductId(),
                cartItemDTO.getSize()
        );


        CartItem finalItem;

        if (!existingItems.isEmpty()) {
            CartItem existingItem = existingItems.get(0); // pick first
            Long newQuantity = existingItem.getQuantity() + cartItemDTO.getQuantity();
            existingItem.setQuantity(newQuantity);
            BigDecimal newTotalPrice = existingItem.getUnitPrice().multiply(BigDecimal.valueOf(newQuantity));
            existingItem.setTotalPrice(newTotalPrice);
            finalItem = cartItemRepository.save(existingItem);
        } else {
            BigDecimal unitPrice = BigDecimal.valueOf(cartItemDTO.getUnitPrice());
            Long quantity = cartItemDTO.getQuantity();
            BigDecimal totalPrice = unitPrice.multiply(BigDecimal.valueOf(quantity));

            CartItem newItem = CartItem.builder()
                    .cart(cart)
                    .productId(cartItemDTO.getProductId())
                    .unitPrice(unitPrice)
                    .quantity(quantity)
                    .totalPrice(totalPrice)
                    .size(cartItemDTO.getSize())
                    .build();

            finalItem = cartItemRepository.save(newItem);
            cart.getCartItems().add(finalItem);
        }


        // Recalculate cart totals
        BigDecimal cartTotalPrice = cart.getCartItems().stream()
                .map(CartItem::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        int totalItems = cart.getCartItems().stream()
                .mapToInt(item -> item.getQuantity().intValue())
                .sum();


        cart.setTotalPrice(cartTotalPrice);
        cart.setTotalItem(totalItems);
        cartRepository.save(cart);

        return CartItemDTO.builder()
                .cartItemId(finalItem.getCartItemId())
                .productId(finalItem.getProductId())
                .unitPrice(finalItem.getUnitPrice().doubleValue())
                .quantity(finalItem.getQuantity())
                .totalPrice(finalItem.getTotalPrice().doubleValue())
                .size(finalItem.getSize())
                .build();
    }

    @Override
    @Transactional
    public CartItemDTO updateCartItemQuantity(CartItemDTO cartItemDTO) {
        if (cartItemDTO.getProductId() == null || cartItemDTO.getCartItemId() == null) {
            throw new IllegalArgumentException("Product ID and Cart Item ID are required.");
        }
        if (cartItemDTO.getQuantity() <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero.");
        }

        CartItem cartItem = cartItemRepository.findByCartItemIdAndProductId(
                cartItemDTO.getCartItemId(),
                cartItemDTO.getProductId()
        ).orElseThrow(() -> new RuntimeException(
                "CartItem not found for CartItem ID: " + cartItemDTO.getCartItemId() +
                        " and Product ID: " + cartItemDTO.getProductId()));

        cartItem.setQuantity(cartItemDTO.getQuantity());
        BigDecimal newTotalPrice = cartItem.getUnitPrice().multiply(BigDecimal.valueOf(cartItemDTO.getQuantity()));
        cartItem.setTotalPrice(newTotalPrice);

        CartItem updatedItem = cartItemRepository.save(cartItem);

        cartItemDTO.setTotalPrice(updatedItem.getTotalPrice().doubleValue());
        return cartItemDTO;
    }

    @Override
    public CartItemDTO findCartItemById(Long id) {
        CartItem cartItem = cartItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("CartItem not found"));

        return CartItemDTO.builder()
                .cartItemId(cartItem.getCartItemId())
                .productId(cartItem.getProductId())
                .quantity(cartItem.getQuantity())
                .unitPrice(cartItem.getUnitPrice().doubleValue())
                .totalPrice(cartItem.getTotalPrice().doubleValue())
                .size(cartItem.getSize())
                .build();
    }

    @Override
    @Transactional
    public void deleteCartItem(Long id) {
        CartItem cartItem = cartItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("CartItem not found"));

        Cart cart = cartItem.getCart();
        cartItemRepository.delete(cartItem);
        cart.getCartItems().remove(cartItem);

        BigDecimal cartTotalPrice = cart.getCartItems().stream()
                .map(CartItem::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        int totalItems = cart.getCartItems().stream()
                .mapToInt(item -> item.getQuantity().intValue())
                .sum();


        cart.setTotalPrice(cartTotalPrice);
        cart.setTotalItem(totalItems);
        cartRepository.save(cart);
    }
}
