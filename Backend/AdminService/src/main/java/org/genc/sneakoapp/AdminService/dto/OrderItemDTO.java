package org.genc.sneakoapp.AdminService.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItemDTO {
    @Nullable
    private Long orderItemId;

    @Nullable
    private Long orderId;


    @NotNull(message = "enter product id")
    private Long productId;
    @NotNull(message = "Enter total quantity")
    private Long quantity;

    @NotNull(message = "enter the unit price of item")
    private BigDecimal unitPrice;
    @Nullable
    private BigDecimal totalPrice;

    @NotNull(message = "Enter the size")
    private Long size;
}