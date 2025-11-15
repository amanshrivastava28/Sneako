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

    @NotNull(message = "Enter the valid user id")
    private Long userId;

    @NotNull(message = "Enter the  valid product id")
    private Long productId;

    @NotNull(message = "unit price cant be empty")
    private double unitPrice;

    @NotNull(message = "quantity should be least one")
    private Long quantity;

    @Nullable
    private double totalPrice;

    @NotNull(message = "Enter the size")
    private Integer size;
}
