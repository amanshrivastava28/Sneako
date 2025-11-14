package org.genc.sneakoapp.cartmanagementservice;

import org.genc.sneakoapp.cartmanagementservice.dto.CartDTO;
import org.genc.sneakoapp.cartmanagementservice.entity.Cart;
import org.genc.sneakoapp.cartmanagementservice.entity.CartItem;
import org.genc.sneakoapp.cartmanagementservice.exception.CartNotFoundException;
import org.genc.sneakoapp.cartmanagementservice.repo.CartRepository;
import org.genc.sneakoapp.cartmanagementservice.service.impl.CartServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.math.BigDecimal;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class CartServiceImplTest {

    @Mock
    private CartRepository cartRepository;

    @InjectMocks
    private CartServiceImpl cartService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetCartByUserId_existingCart_returnsCartDTO() {
        // Arrange
        CartItem item = CartItem.builder()
                .cartItemId(1L)
                .productId(100L)
                .unitPrice(BigDecimal.valueOf(50))
                .quantity(2L)
                .totalPrice(BigDecimal.valueOf(100))
                .size(10)
                .build();

        Cart cart = Cart.builder()
                .id(1L)
                .userId(10L)
                .totalPrice(BigDecimal.valueOf(100))
                .totalItem(2)
                .cartItems(Set.of(item))
                .build();

        when(cartRepository.findByUserId(10L)).thenReturn(Optional.of(cart));

        // Act
        CartDTO result = cartService.getCartByUserId(10L);

        // Assert
        assertThat(result.getUserId()).isEqualTo(10L);
        assertThat(result.getTotalPrice()).isEqualTo(100.0);
        assertThat(result.getCartItems()).hasSize(1);
        verify(cartRepository, times(1)).findByUserId(10L);
    }

    @Test
    void testGetCartByUserId_noCart_createsNewCart() {
        // Arrange
        when(cartRepository.findByUserId(20L)).thenReturn(Optional.empty());

        Cart newCart = Cart.builder()
                .id(2L)
                .userId(20L)
                .totalPrice(BigDecimal.ZERO)
                .totalItem(0)
                .cartItems(new HashSet<>())
                .build();

        when(cartRepository.save(any(Cart.class))).thenReturn(newCart);

        // Act
        CartDTO result = cartService.getCartByUserId(20L);

        // Assert
        assertThat(result.getUserId()).isEqualTo(20L);
        assertThat(result.getTotalPrice()).isEqualTo(0.0);
        assertThat(result.getCartItems()).isEmpty();
        verify(cartRepository, times(1)).findByUserId(20L);
        verify(cartRepository, times(1)).save(any(Cart.class));
    }

    @Test
    void testRecalculateCart_existingCart_updatesTotals() {
        // Arrange
        CartItem item1 = CartItem.builder()
                .cartItemId(1L)
                .productId(101L)
                .unitPrice(BigDecimal.valueOf(20))
                .quantity(1L)
                .totalPrice(BigDecimal.valueOf(20))
                .size(9)
                .build();

        CartItem item2 = CartItem.builder()
                .cartItemId(2L)
                .productId(102L)
                .unitPrice(BigDecimal.valueOf(30))
                .quantity(2L)
                .totalPrice(BigDecimal.valueOf(60))
                .size(9)
                .build();

        Cart cart = Cart.builder()
                .id(3L)
                .userId(30L)
                .cartItems(Set.of(item1, item2))
                .build();

        when(cartRepository.findById(3L)).thenReturn(Optional.of(cart));
        when(cartRepository.findByUserId(30L)).thenReturn(Optional.of(cart));
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);

        // Act
        CartDTO result = cartService.recalculateCart(3L);

        // Assert
        assertThat(result.getTotalPrice()).isEqualTo(80.0); // 20 + 60
        assertThat(result.getTotalItem()).isEqualTo(2);     // two items
        verify(cartRepository, times(1)).findById(3L);
        verify(cartRepository, times(1)).save(cart);
    }

    @Test
    void testRecalculateCart_cartNotFound_throwsException() {
        // Arrange
        when(cartRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(CartNotFoundException.class, () -> cartService.recalculateCart(99L));
        verify(cartRepository, times(1)).findById(99L);
    }
}
