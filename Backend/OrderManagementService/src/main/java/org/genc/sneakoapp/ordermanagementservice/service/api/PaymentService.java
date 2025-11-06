package org.genc.sneakoapp.ordermanagementservice.service.api;


import org.genc.sneakoapp.ordermanagementservice.dto.PaymentDTO;

public interface PaymentService {
    public PaymentDTO createPayment(PaymentDTO paymentDTO);
}
