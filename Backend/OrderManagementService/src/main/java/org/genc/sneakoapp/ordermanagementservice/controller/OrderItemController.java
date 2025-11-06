package org.genc.sneakoapp.ordermanagementservice.controller;

import lombok.RequiredArgsConstructor;

import org.genc.sneakoapp.ordermanagementservice.dto.OrderItemDTO;
import org.genc.sneakoapp.ordermanagementservice.service.api.OrderItemService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/order-service/order-items")
@RequiredArgsConstructor
public class OrderItemController {

    private final OrderItemService orderItemService;

    @PostMapping
    public ResponseEntity<OrderItemDTO> createOrderItem(@RequestBody OrderItemDTO dto) {
        return new ResponseEntity<>(orderItemService.createOrderItem(dto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrderItemDTO> updateOrderItem(
            @PathVariable Long id, @RequestBody OrderItemDTO dto) {
        return new ResponseEntity<>(orderItemService.updateOrderItem(id, dto), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderItemDTO> getOrderItemById(@PathVariable Long id) {
        return new ResponseEntity<>(orderItemService.findOrderItemById(id), HttpStatus.OK);
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<List<OrderItemDTO>> getItemsByOrderId(@PathVariable Long orderId) {
        return new ResponseEntity<>(orderItemService.findItemsByOrderId(orderId), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrderItem(@PathVariable Long id) {
        orderItemService.deleteOrderItem(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}