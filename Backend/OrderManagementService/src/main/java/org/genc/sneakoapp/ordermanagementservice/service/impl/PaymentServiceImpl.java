package org.genc.sneakoapp.ordermanagementservice.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.genc.sneakoapp.ordermanagementservice.dto.PaymentDTO;
import org.genc.sneakoapp.ordermanagementservice.entity.Payment;
import org.genc.sneakoapp.ordermanagementservice.repo.PaymentRepository;
import org.genc.sneakoapp.ordermanagementservice.service.api.PaymentService;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;

    @Override
    public PaymentDTO createPayment(PaymentDTO paymentDTO) {
        Payment paymentEntity=new Payment();
        paymentEntity.setPaymentId(paymentDTO.getPaymentId());
        paymentEntity.setOrderId(paymentDTO.getOrderId());
        paymentEntity.setPaymentDate(paymentDTO.getPaymentDate());
        paymentEntity.setPaymentMethod(paymentDTO.getPaymentMethod());
        paymentEntity.setTransactionId(paymentDTO.getTransactionId());
        paymentEntity.setUserId(paymentDTO.getUserId());

        Payment paymentObj=paymentRepository.save(paymentEntity);
        log.info("New Category creted with the :{}",paymentObj.getPaymentId());
        PaymentDTO responseDTO=new PaymentDTO(paymentObj.getPaymentId(),paymentObj.getOrderId(),
                paymentObj.getTransactionId(),paymentObj.getPaymentMethod(),paymentObj.getPaymentDate(),paymentObj.getUserId());
        return responseDTO;
    }
}
