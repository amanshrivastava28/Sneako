package org.genc.sneakoapp.ordermanagementservice.dto;


import jakarta.annotation.Nullable;
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

    private Long orderId;

    private String transactionId;

    private String paymentMethod;

    private String paymentDate;

}
