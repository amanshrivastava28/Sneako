package org.genc.sneakoapp.ordermanagementservice.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.*;

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


    @NotNull
    private Long productId;
    @NotNull
    private Long quantity;
    @NotNull
    private BigDecimal unitPrice;

    private BigDecimal totalPrice;
    private Long size;
}