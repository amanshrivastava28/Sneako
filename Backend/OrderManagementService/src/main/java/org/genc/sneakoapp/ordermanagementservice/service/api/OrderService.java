package org.genc.sneakoapp.ordermanagementservice.service.api;

import org.genc.sneakoapp.ordermanagementservice.dto.OrderDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OrderService {
  public OrderDTO createOrder(OrderDTO orderDTO);
  public   OrderDTO findOrderById(Long id);
  public Page<OrderDTO> getOrders(Pageable pageable);
  public OrderDTO updateOrderStatus(Long orderId, String newStatus);
  public Long calculateTotalRevenue();
  public Long totalOrders();
    public Long calculateTotalRevenu();
  List<OrderDTO> findOrdersByUserId(Long userId);





}
