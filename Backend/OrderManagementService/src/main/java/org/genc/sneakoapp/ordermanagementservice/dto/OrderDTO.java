package org.genc.sneakoapp.ordermanagementservice.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDTO {
    @Nullable
    private Long orderId;
    @NotNull
    private Long userId;

    @NotNull
    private String shippingAddress;

    @Nullable
    private BigDecimal totalPrice;

    @NotNull
    private String orderStatus;
    private LocalDateTime orderDate;
    private List<OrderItemDTO> orderItems;

    public OrderDTO(Long orderId, Long userId, String shippingAddress, BigDecimal totalPrice, String orderStatus, LocalDateTime orderDate) {
    }
}