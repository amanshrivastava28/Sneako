package org.genc.sneakoapp.ordermanagementservice;

import org.genc.sneakoapp.ordermanagementservice.dto.OrderDTO;
import org.genc.sneakoapp.ordermanagementservice.dto.OrderItemDTO;
import org.genc.sneakoapp.ordermanagementservice.entity.Order;
import org.genc.sneakoapp.ordermanagementservice.entity.OrderItem;
import org.genc.sneakoapp.ordermanagementservice.repo.OrderItemRepository;
import org.genc.sneakoapp.ordermanagementservice.repo.OrderRepository;
import org.genc.sneakoapp.ordermanagementservice.service.impl.OrderServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for OrderServiceImpl (excluding createOrder tests as requested).
 */
@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderItemRepository orderItemRepository;

    @InjectMocks
    private OrderServiceImpl orderService;

    @BeforeEach
    void setUp() {
        // Mocks injected by MockitoExtension
    }

    @Test
    void findOrderById_existingOrder_mapsToDto() {
        // Arrange
        OrderItem item = OrderItem.builder()
                .orderItemId(21L)
                .productId(200L)
                .quantity(1L)
                .unitPrice(new BigDecimal("5.00"))
                .totalPrice(new BigDecimal("5.00"))
                .size(7L)
                .build();

        Order order = Order.builder()
                .orderId(2L)
                .userId(99L)
                .shippingAddress("Somewhere")
                .orderStatus("NEW")
                .totalPrice(new BigDecimal("5.00"))
                .orderDate(LocalDateTime.now())
                .orderItems(List.of(item))
                .build();

        // ensure back-reference if required by your entity
        item.setOrder(order);

        when(orderRepository.findById(2L)).thenReturn(Optional.of(order));

        // Act
        OrderDTO dto = orderService.findOrderById(2L);

        // Assert
        assertThat(dto).isNotNull();
        assertThat(dto.getOrderId()).isEqualTo(2L);
        assertThat(dto.getUserId()).isEqualTo(99L);
        assertThat(dto.getOrderItems()).hasSize(1);
        assertThat(dto.getOrderItems().get(0).getProductId()).isEqualTo(200L);

        verify(orderRepository).findById(2L);
        verifyNoMoreInteractions(orderRepository);
        verifyNoInteractions(orderItemRepository);
    }

    @Test
    void getOrders_returnsPagedOrderDTOs() {
        // Arrange
        Order order = Order.builder()
                .orderId(3L)
                .userId(5L)
                .orderStatus("NEW")
                .orderDate(LocalDateTime.now())
                .orderItems(List.of())
                .build();

        Pageable pageable = PageRequest.of(0, 10);
        Page<Order> page = new PageImpl<>(List.of(order), pageable, 1L);

        when(orderRepository.findAll(pageable)).thenReturn(page);

        // Act
        Page<OrderDTO> result = orderService.getOrders(pageable);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent().get(0).getOrderId()).isEqualTo(3L);

        verify(orderRepository).findAll(pageable);
        verifyNoMoreInteractions(orderRepository);
        verifyNoInteractions(orderItemRepository);
    }

    @Test
    void updateOrderStatus_changesStatusAndReturnsDto() {
        // Arrange
        Order existing = Order.builder()
                .orderId(4L)
                .userId(7L)
                .orderStatus("PENDING")
                .orderItems(List.of())
                .build();

        when(orderRepository.findById(4L)).thenReturn(Optional.of(existing));
        when(orderRepository.save(any(Order.class))).thenAnswer(inv -> inv.getArgument(0));

        // Act
        OrderDTO updated = orderService.updateOrderStatus(4L, "SHIPPED");

        // Assert
        assertThat(updated).isNotNull();
        assertThat(updated.getOrderStatus()).isEqualTo("SHIPPED");
        verify(orderRepository).findById(4L);
        verify(orderRepository).save(any(Order.class));
        verifyNoMoreInteractions(orderRepository);
        verifyNoInteractions(orderItemRepository);
    }

    @Test
    void calculateTotalRevenue_sumsAllOrderTotals() {
        // Arrange
        Order o1 = Order.builder().orderId(10L).totalPrice(new BigDecimal("10.50")).build();
        Order o2 = Order.builder().orderId(11L).totalPrice(new BigDecimal("5.25")).build();
        when(orderRepository.findAll()).thenReturn(List.of(o1, o2));

        // Act
        Long total = orderService.calculateTotalRevenue();

        // Assert: 10.50 + 5.25 = 15.75 -> longValue = 15
        assertThat(total).isEqualTo(new BigDecimal("15.75").longValue());
        verify(orderRepository).findAll();
        verifyNoMoreInteractions(orderRepository);
        verifyNoInteractions(orderItemRepository);
    }

    @Test
    void totalOrders_returnsRepositoryCount() {
        when(orderRepository.count()).thenReturn(42L);
        Long result = orderService.totalOrders();
        assertThat(result).isEqualTo(42L);
        verify(orderRepository).count();
        verifyNoMoreInteractions(orderRepository);
        verifyNoInteractions(orderItemRepository);
    }

    @Test
    void findOrdersByUserId_mapsOrdersToDtoList() {
        // Arrange
        Order o1 = Order.builder().orderId(101L).userId(300L).orderStatus("DONE").orderItems(List.of()).build();
        Order o2 = Order.builder().orderId(102L).userId(300L).orderStatus("CANCELLED").orderItems(List.of()).build();
        when(orderRepository.findByUserId(300L)).thenReturn(List.of(o1, o2));

        // Act
        List<OrderDTO> dtos = orderService.findOrdersByUserId(300L);

        // Assert
        assertThat(dtos).hasSize(2);
        assertThat(dtos).extracting(OrderDTO::getOrderId).containsExactly(101L, 102L);
        verify(orderRepository).findByUserId(300L);
        verifyNoMoreInteractions(orderRepository);
        verifyNoInteractions(orderItemRepository);
    }
}