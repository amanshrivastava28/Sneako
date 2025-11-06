package org.genc.sneakoapp.AdminService.dto;

import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private Long userId;
    private String shippingAddress;

    @Nullable
    private BigDecimal totalPrice;
    private String orderStatus;
    private LocalDateTime orderDate;
    private List<OrderItemDTO> orderItems;

    public OrderDTO(Long orderId, Long userId, String shippingAddress, BigDecimal totalPrice, String orderStatus, LocalDateTime orderDate) {
    }
}