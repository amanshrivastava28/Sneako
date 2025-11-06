package org.genc.sneakoapp.cartmanagementservice.dto;

import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItemDTO {
    @Nullable
    private Long cartItemId;
    private Long userId;
    private Long productId;
    private double unitPrice;
    private Long quantity;

    @Nullable
    private double totalPrice;
    private Integer size; // Added size field
}
