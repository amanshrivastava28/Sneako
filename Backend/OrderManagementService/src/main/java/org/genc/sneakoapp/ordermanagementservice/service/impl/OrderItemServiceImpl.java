package org.genc.sneakoapp.ordermanagementservice.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.genc.sneakoapp.ordermanagementservice.dto.OrderItemDTO;
import org.genc.sneakoapp.ordermanagementservice.entity.Order;
import org.genc.sneakoapp.ordermanagementservice.entity.OrderItem;
import org.genc.sneakoapp.ordermanagementservice.repo.OrderItemRepository;
import org.genc.sneakoapp.ordermanagementservice.repo.OrderRepository;
import org.genc.sneakoapp.ordermanagementservice.service.api.OrderItemService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderItemServiceImpl implements OrderItemService {

    private final OrderItemRepository orderItemRepository;
    private final OrderRepository orderRepository;

    @Override
    @Transactional
    public OrderItemDTO createOrderItem(OrderItemDTO dto) {
        Order order = orderRepository.findById(dto.getOrderId())
                .orElseThrow(() -> new RuntimeException("Order not found"));

        BigDecimal total = dto.getUnitPrice().multiply(BigDecimal.valueOf(dto.getQuantity()));

        OrderItem orderItem = OrderItem.builder()
                .order(order)
                .productId(dto.getProductId())
                .quantity(dto.getQuantity())
                .unitPrice(dto.getUnitPrice())
                .totalPrice(total)
                .size(dto.getSize())
                .build();

        orderItemRepository.save(orderItem);

        updateOrderTotal(order);

        return convertToDTO(orderItem);
    }

    @Override
    @Transactional
    public OrderItemDTO updateOrderItem(Long id, OrderItemDTO dto) {
        OrderItem item = orderItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order item not found"));

        if (dto.getQuantity() != null) item.setQuantity(dto.getQuantity());
        if (dto.getUnitPrice() != null) item.setUnitPrice(dto.getUnitPrice());
        if (dto.getSize() != null) item.setSize(dto.getSize());

        // Recalculate item total
        item.setTotalPrice(item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
        orderItemRepository.save(item);

        updateOrderTotal(item.getOrder());

        return convertToDTO(item);
    }

    @Override
    @Transactional
    public void deleteOrderItem(Long id) {
        OrderItem item = orderItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order item not found"));

        Order order = item.getOrder();
        orderItemRepository.delete(item);

        updateOrderTotal(order);
    }

    @Override
    public OrderItemDTO findOrderItemById(Long id) {
        OrderItem item = orderItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order item not found"));
        return convertToDTO(item);
    }

    @Override
    public List<OrderItemDTO> findItemsByOrderId(Long orderId) {
        List<OrderItem> items = orderItemRepository.findByOrderOrderId(orderId);
        return items.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // ✅ Utility method to recalc order total
    private void updateOrderTotal(Order order) {
        BigDecimal orderTotal = orderItemRepository.findByOrderOrderId(order.getOrderId())
                .stream()
                .map(OrderItem::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        order.setTotalPrice(orderTotal);
        orderRepository.save(order);
    }

    private OrderItemDTO convertToDTO(OrderItem item) {
        return OrderItemDTO.builder()
                .orderItemId(item.getOrderItemId())
                .orderId(item.getOrder().getOrderId())
                .productId(item.getProductId())
                .quantity(item.getQuantity())
                .unitPrice(item.getUnitPrice())
                .totalPrice(item.getTotalPrice())
                .size(item.getSize())
                .build();
    }
}