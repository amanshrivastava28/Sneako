package org.genc.sneakoapp.cartmanagementservice.service.api;


import org.genc.sneakoapp.cartmanagementservice.dto.CartItemDTO;

public interface CartItemService {
    CartItemDTO createCartItem(CartItemDTO cartItemDTO);
    CartItemDTO updateCartItemQuantity(CartItemDTO cartItemDTO);
    CartItemDTO findCartItemById(Long id);
    void deleteCartItem(Long id);
}
