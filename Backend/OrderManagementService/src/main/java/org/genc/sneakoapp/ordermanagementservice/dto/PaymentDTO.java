package org.genc.sneakoapp.ordermanagementservice.dto;


import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentDTO {

    @Nullable
    private Long paymentId;

    @NotNull
    private Long orderId;

    @NotNull
    private String transactionId;

    @NotNull
    private String paymentMethod;

    @NotNull
    private String paymentDate;

    @NotNull
    private Long userId;

}
