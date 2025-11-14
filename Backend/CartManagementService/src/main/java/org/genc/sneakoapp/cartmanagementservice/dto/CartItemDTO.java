package org.genc.sneakoapp.cartmanagementservice.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
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

    @NotNull
    private Long userId;

    @NotNull
    private Long productId;

    @NotNull
    private double unitPrice;

    @NotNull
    private Long quantity;

    @Nullable
    private double totalPrice;

    @NotNull
    private Integer size;
}
