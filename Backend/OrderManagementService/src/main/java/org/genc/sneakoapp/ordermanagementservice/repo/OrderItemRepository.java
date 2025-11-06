package org.genc.sneakoapp.ordermanagementservice.repo;


import org.genc.sneakoapp.ordermanagementservice.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    List<OrderItem> findByOrderOrderId(Long orderId);
}