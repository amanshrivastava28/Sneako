package org.genc.sneakoapp.ordermanagementservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.genc.sneakoapp.ordermanagementservice.dto.OrderDTO;
import org.genc.sneakoapp.ordermanagementservice.dto.OrderItemDTO;
import org.genc.sneakoapp.ordermanagementservice.service.api.OrderService;
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
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Updated OrderControllerTest.
 *
 * Key change: instantiate PageImpl with a concrete Pageable (PageRequest.of(...))
 * instead of using the default unpaged PageImpl constructor. Unpaged (org.springframework.data.domain.Unpaged)
 * has an offset() method that throws UnsupportedOperationException; Jackson attempted to serialize
 * the pageable and hit that exception. Using a concrete PageRequest prevents that.
 */
@ExtendWith(MockitoExtension.class)
public class OrderControllerTest {

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private OrderService orderService;

    @InjectMocks
    private org.genc.sneakoapp.ordermanagementservice.controller.OrderController orderController;

    @BeforeEach
    void setup() {
        // Configure ObjectMapper similar to Spring Boot: register Java Time support and auto modules
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.findAndRegisterModules();

        MappingJackson2HttpMessageConverter jacksonConverter =
                new MappingJackson2HttpMessageConverter(objectMapper);

        mockMvc = MockMvcBuilders.standaloneSetup(orderController)
                .setMessageConverters(jacksonConverter)
                .build();
    }

    @Test
    void getOrders_returnsPagedOrders() throws Exception {
        OrderDTO order = OrderDTO.builder()
                .orderId(1L)
                .userId(2L)
                .orderStatus("NEW")
                .totalPrice(new BigDecimal("42.00"))
                .orderDate(LocalDateTime.now())
                .build();

        // Use a concrete PageRequest to avoid Unpaged serialization issues
        Pageable pageable = PageRequest.of(0, 10);
        Page<OrderDTO> page = new PageImpl<>(List.of(order), pageable, 1L);
        when(orderService.getOrders(any(Pageable.class))).thenReturn(page);

        MvcResult mvcResult = mockMvc.perform(get("/api/v1/order-service/order")
                        .param("page", "0")
                        .param("size", "10")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(result -> {
                    if (result.getResolvedException() != null) {
                        System.err.println("Resolved exception during serialization:");
                        result.getResolvedException().printStackTrace();
                    }
                })
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].orderId").value(1))
                .andExpect(jsonPath("$.content[0].userId").value(2))
                .andExpect(jsonPath("$.content[0].orderStatus").value("NEW"))
                .andReturn();

        verify(orderService, times(1)).getOrders(any(Pageable.class));
        verifyNoMoreInteractions(orderService);
    }

    @Test
    void createOrder_returnsCreatedOrder() throws Exception {
        OrderItemDTO item = OrderItemDTO.builder()
                .orderItemId(11L)
                .productId(100L)
                .quantity(1L)
                .unitPrice(new BigDecimal("15.50"))
                .totalPrice(new BigDecimal("15.50"))
                .build();

        OrderDTO input = OrderDTO.builder()
                .userId(2L)
                .orderStatus("NEW")
                .totalPrice(new BigDecimal("15.50"))
                .orderItems(List.of(item))
                .build();

        when(orderService.createOrder(any(OrderDTO.class))).thenAnswer(inv -> {
            OrderDTO arg = inv.getArgument(0);
            arg.setOrderId(5L);
            arg.setOrderDate(LocalDateTime.now());
            return arg;
        });

        mockMvc.perform(post("/api/v1/order-service/order")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andDo(result -> {
                    if (result.getResolvedException() != null) {
                        System.err.println("Resolved exception during serialization:");
                        result.getResolvedException().printStackTrace();
                    }
                })
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.orderId").value(5))
                .andExpect(jsonPath("$.userId").value(2))
                .andExpect(jsonPath("$.orderStatus").value("NEW"));

        verify(orderService, times(1)).createOrder(any(OrderDTO.class));
        verifyNoMoreInteractions(orderService);
    }

    @Test
    void getOrderById_returnsOrder() throws Exception {
        OrderDTO order = OrderDTO.builder()
                .orderId(10L)
                .userId(20L)
                .orderStatus("SHIPPED")
                .totalPrice(new BigDecimal("100.00"))
                .orderDate(LocalDateTime.now())
                .build();

        when(orderService.findOrderById(10L)).thenReturn(order);

        mockMvc.perform(get("/api/v1/order-service/order/10")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderId").value(10))
                .andExpect(jsonPath("$.userId").value(20))
                .andExpect(jsonPath("$.orderStatus").value("SHIPPED"));

        verify(orderService, times(1)).findOrderById(10L);
        verifyNoMoreInteractions(orderService);
    }

    @Test
    void getOrdersByUserId_returnsListOfOrders() throws Exception {
        OrderDTO o1 = OrderDTO.builder()
                .orderId(101L)
                .userId(300L)
                .orderStatus("DELIVERED")
                .build();

        OrderDTO o2 = OrderDTO.builder()
                .orderId(102L)
                .userId(300L)
                .orderStatus("CANCELLED")
                .build();

        List<OrderDTO> orders = List.of(o1, o2);
        when(orderService.findOrdersByUserId(300L)).thenReturn(orders);

        mockMvc.perform(get("/api/v1/order-service/order/user/300")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].orderId").value(101))
                .andExpect(jsonPath("$[0].userId").value(300))
                .andExpect(jsonPath("$[1].orderId").value(102))
                .andExpect(jsonPath("$[1].orderStatus").value("CANCELLED"));

        verify(orderService, times(1)).findOrdersByUserId(300L);
        verifyNoMoreInteractions(orderService);
    }
}