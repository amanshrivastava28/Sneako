package org.genc.sneakoapp.cartmanagementservice.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartDTO {
    @Nullable
    private Long id;

    @NotNull(message = "enter the user id")
    private Long userId;

    @NotNull(message = "least one cart item should be there")
    private Set<CartItemDTO> cartItems;


    private double totalPrice;

    @NotNull
    private Integer totalItem;
}
