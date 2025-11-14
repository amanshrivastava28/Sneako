package org.genc.sneakoapp.ordermanagementservice;

import org.genc.sneakoapp.ordermanagementservice.dto.OrderItemDTO;
import org.genc.sneakoapp.ordermanagementservice.entity.Order;
import org.genc.sneakoapp.ordermanagementservice.entity.OrderItem;
import org.genc.sneakoapp.ordermanagementservice.exception.OrderNotFoundException;
import org.genc.sneakoapp.ordermanagementservice.exception.OrderItemNotFoundException;
import org.genc.sneakoapp.ordermanagementservice.repo.OrderItemRepository;
import org.genc.sneakoapp.ordermanagementservice.repo.OrderRepository;
import org.genc.sneakoapp.ordermanagementservice.service.impl.OrderItemServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.math.BigDecimal;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class OrderItemServiceImplTest {

    @Mock
    private OrderItemRepository orderItemRepository;

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderItemServiceImpl orderItemService;

    private Order order;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        order = Order.builder()
                .orderId(1L)
                .totalPrice(BigDecimal.ZERO)
                .build();
    }

    @Test
    void testCreateOrderItem_success() {
        OrderItemDTO dto = OrderItemDTO.builder()
                .orderId(1L)
                .productId(100L)
                .quantity(2L)
                .unitPrice(BigDecimal.valueOf(50))
                .size(9L)
                .build();

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(orderItemRepository.findByOrderOrderId(1L)).thenReturn(Collections.emptyList());
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        OrderItemDTO result = orderItemService.createOrderItem(dto);

        assertThat(result.getProductId()).isEqualTo(100L);
        assertThat(result.getTotalPrice()).isEqualTo(BigDecimal.valueOf(100));
        verify(orderItemRepository, times(1)).save(any(OrderItem.class));
        verify(orderRepository, times(1)).save(order);
    }

    @Test
    void testCreateOrderItem_orderNotFound_throwsException() {
        OrderItemDTO dto = OrderItemDTO.builder()
                .orderId(99L)
                .productId(100L)
                .quantity(1L)
                .unitPrice(BigDecimal.TEN)
                .build();

        when(orderRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(OrderNotFoundException.class, () -> orderItemService.createOrderItem(dto));
    }

    @Test
    void testUpdateOrderItem_success() {
        OrderItem existing = OrderItem.builder()
                .orderItemId(1L)
                .order(order)
                .productId(100L)
                .quantity(1L)
                .unitPrice(BigDecimal.TEN)
                .totalPrice(BigDecimal.TEN)
                .size(9L)
                .build();

        OrderItemDTO dto = OrderItemDTO.builder()
                .quantity(3L)
                .unitPrice(BigDecimal.valueOf(20))
                .size(9L)
                .build();

        when(orderItemRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(orderItemRepository.findByOrderOrderId(1L)).thenReturn(List.of(existing));
        when(orderRepository.save(order)).thenReturn(order);

        OrderItemDTO result = orderItemService.updateOrderItem(1L, dto);

        assertThat(result.getQuantity()).isEqualTo(3L);
        assertThat(result.getUnitPrice()).isEqualTo(BigDecimal.valueOf(20));
        assertThat(result.getTotalPrice()).isEqualTo(BigDecimal.valueOf(60));
        verify(orderItemRepository, times(1)).save(existing);
    }

    @Test
    void testUpdateOrderItem_notFound_throwsException() {
        when(orderItemRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(OrderItemNotFoundException.class,
                () -> orderItemService.updateOrderItem(99L, OrderItemDTO.builder().build()));
    }

    @Test
    void testDeleteOrderItem_success() {
        OrderItem existing = OrderItem.builder()
                .orderItemId(1L)
                .order(order)
                .productId(100L)
                .quantity(1L)
                .unitPrice(BigDecimal.TEN)
                .totalPrice(BigDecimal.TEN)
                .build();

        when(orderItemRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(orderItemRepository.findByOrderOrderId(1L)).thenReturn(Collections.emptyList());

        orderItemService.deleteOrderItem(1L);

        verify(orderItemRepository, times(1)).delete(existing);
        verify(orderRepository, times(1)).save(order);
    }

    @Test
    void testDeleteOrderItem_notFound_throwsException() {
        when(orderItemRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(OrderItemNotFoundException.class, () -> orderItemService.deleteOrderItem(99L));
    }

    @Test
    void testFindOrderItemById_success() {
        OrderItem existing = OrderItem.builder()
                .orderItemId(1L)
                .order(order)
                .productId(100L)
                .quantity(2L)
                .unitPrice(BigDecimal.valueOf(50))
                .totalPrice(BigDecimal.valueOf(100))
                .size(9L)
                .build();

        when(orderItemRepository.findById(1L)).thenReturn(Optional.of(existing));

        OrderItemDTO result = orderItemService.findOrderItemById(1L);

        assertThat(result.getOrderItemId()).isEqualTo(1L);
        assertThat(result.getTotalPrice()).isEqualTo(BigDecimal.valueOf(100));
    }

    @Test
    void testFindOrderItemById_notFound_throwsException() {
        when(orderItemRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(OrderItemNotFoundException.class, () -> orderItemService.findOrderItemById(99L));
    }

    @Test
    void testFindItemsByOrderId_returnsList() {
        OrderItem item1 = OrderItem.builder()
                .orderItemId(1L)
                .order(order)
                .productId(100L)
                .quantity(1L)
                .unitPrice(BigDecimal.TEN)
                .totalPrice(BigDecimal.TEN)
                .build();

        OrderItem item2 = OrderItem.builder()
                .orderItemId(2L)
                .order(order)
                .productId(200L)
                .quantity(2L)
                .unitPrice(BigDecimal.valueOf(20))
                .totalPrice(BigDecimal.valueOf(40))
                .build();

        when(orderItemRepository.findByOrderOrderId(1L)).thenReturn(List.of(item1, item2));

        List<OrderItemDTO> result = orderItemService.findItemsByOrderId(1L);

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getProductId()).isEqualTo(100L);
        assertThat(result.get(1).getProductId()).isEqualTo(200L);
    }
}
