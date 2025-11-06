package org.genc.sneakoapp.AdminService.repo;


import org.genc.sneakoapp.AdminService.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
}