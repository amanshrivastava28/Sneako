package org.genc.sneakoapp.ordermanagementservice.repo;


import org.genc.sneakoapp.ordermanagementservice.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface  PaymentRepository extends JpaRepository<Payment,Long> {
}
