package com.orderflow.backend.controller;

import com.orderflow.backend.dto.OrderResponse;
import com.orderflow.backend.service.OrderService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(
            OrderService orderService) {

        this.orderService = orderService;
    }

    // Checkout current user's cart
    @PostMapping("/checkout")
    public ResponseEntity<OrderResponse> checkout(
            Authentication authentication) {

        String email =
                authentication.getName();

        OrderResponse order =
                orderService.checkout(email);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(order);
    }

    // Get current user's order history
    @GetMapping
    public ResponseEntity<List<OrderResponse>>
    getOrderHistory(
            Authentication authentication) {

        String email =
                authentication.getName();

        List<OrderResponse> orders =
                orderService.getOrderHistory(
                        email
                );

        return ResponseEntity.ok(
                orders
        );
    }
}