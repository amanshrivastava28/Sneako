package org.genc.sneakoapp.cartmanagementservice.service.api;


import org.genc.sneakoapp.cartmanagementservice.dto.CartDTO;

public interface CartService {
    CartDTO getCartByUserId(Long userId);
    CartDTO recalculateCart(Long cartId);
}
