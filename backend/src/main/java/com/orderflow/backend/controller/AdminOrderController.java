package com.orderflow.backend.controller;

import com.orderflow.backend.dto.OrderResponse;
import com.orderflow.backend.dto.UpdateOrderStatusRequest;
import com.orderflow.backend.service.OrderService;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/orders")
public class AdminOrderController {

    private final OrderService orderService;

    public AdminOrderController(
            OrderService orderService) {

        this.orderService =
                orderService;
    }

    @GetMapping
    public ResponseEntity<List<OrderResponse>>
    getAllOrders() {

        return ResponseEntity.ok(
                orderService
                        .getAllOrders()
        );
    }

    @PutMapping("/{orderId}/status")
    public ResponseEntity<OrderResponse>
    updateOrderStatus(
            @PathVariable Long orderId,
            @Valid
            @RequestBody
            UpdateOrderStatusRequest request) {

        OrderResponse order =
                orderService
                        .updateOrderStatus(
                                orderId,
                                request.getStatus()
                        );

        return ResponseEntity.ok(
                order
        );
    }
}