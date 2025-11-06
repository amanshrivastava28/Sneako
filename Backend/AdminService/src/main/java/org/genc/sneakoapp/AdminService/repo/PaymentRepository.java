package org.genc.sneakoapp.AdminService.repo;


import org.genc.sneakoapp.AdminService.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface  PaymentRepository extends JpaRepository<Payment,Long> {
}
