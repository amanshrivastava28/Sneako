package org.genc.sneakoapp.ordermanagementservice.dto;

import jakarta.annotation.Nullable;
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



    private Long productId;
    private Long quantity;
    private BigDecimal unitPrice;
    private BigDecimal totalPrice;
    private Long size;
}