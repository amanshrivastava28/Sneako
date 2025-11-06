package org.genc.sneakoapp.cartmanagementservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.genc.sneakoapp.cartmanagementservice.dto.CartItemDTO;
import org.genc.sneakoapp.cartmanagementservice.entity.CartItem;
import org.genc.sneakoapp.cartmanagementservice.repo.CartItemRepository;
import org.genc.sneakoapp.cartmanagementservice.service.api.CartItemService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/cart-service")
@RequiredArgsConstructor
public class CartItemController
{
    private final CartItemService cartItemService;
    private final CartItemRepository cartItemRepository;


    @PostMapping("cart-item")
    public ResponseEntity<CartItemDTO> addItemToCart(@Valid @RequestBody CartItemDTO cartItemDTO) {

        return  new ResponseEntity<>(cartItemService.createCartItem(cartItemDTO), HttpStatus.CREATED);

    }
    @PatchMapping("cart-item")
    public ResponseEntity<CartItemDTO> updateCartItemQuantity(@RequestBody CartItemDTO cartItemDTO) {
        // Returns 200 OK for a successful update operation
        CartItemDTO updatedItem = cartItemService.updateCartItemQuantity(cartItemDTO);
        return new ResponseEntity<>(updatedItem, HttpStatus.OK);
    }

    @GetMapping("cart-item/{id}")
    public ResponseEntity<CartItemDTO> findCartItemById(@PathVariable Long id)
    {
        return  new ResponseEntity<>(cartItemService.findCartItemById(id),HttpStatus.OK);
    }

    @DeleteMapping("cart-item/{id}")
    public ResponseEntity<Void> deleteCartItem(@PathVariable Long id) {
        cartItemService.deleteCartItem(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
