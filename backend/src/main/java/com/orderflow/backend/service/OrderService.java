package com.orderflow.backend.service;

import com.orderflow.backend.dto.OrderItemResponse;
import com.orderflow.backend.dto.OrderResponse;
import com.orderflow.backend.exception.ResourceNotFoundException;
import com.orderflow.backend.model.CartItem;
import com.orderflow.backend.model.Order;
import com.orderflow.backend.model.OrderItem;
import com.orderflow.backend.model.OrderStatus;
import com.orderflow.backend.model.Product;
import com.orderflow.backend.model.User;
import com.orderflow.backend.repository.CartItemRepository;
import com.orderflow.backend.repository.OrderItemRepository;
import com.orderflow.backend.repository.OrderRepository;
import com.orderflow.backend.repository.ProductRepository;
import com.orderflow.backend.repository.UserRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public OrderService(
            OrderRepository orderRepository,
            OrderItemRepository orderItemRepository,
            CartItemRepository cartItemRepository,
            ProductRepository productRepository,
            UserRepository userRepository) {

        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.cartItemRepository = cartItemRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public OrderResponse checkout(
            String email) {

        User user =
                getUserByEmail(email);

        List<CartItem> cartItems =
                cartItemRepository
                        .findByUser(user);

        if (cartItems.isEmpty()) {
            throw new IllegalArgumentException(
                    "Cannot checkout an empty cart"
            );
        }

        /*
         * Validate the complete cart before
         * changing inventory.
         */
        for (CartItem cartItem : cartItems) {

            Product product =
                    cartItem.getProduct();

            if (!Boolean.TRUE.equals(
                    product.getActive())) {

                throw new IllegalArgumentException(
                        "Product is no longer available: "
                                + product.getName()
                );
            }

            if (cartItem.getQuantity()
                    > product.getStockQuantity()) {

                throw new IllegalArgumentException(
                        "Insufficient stock for product: "
                                + product.getName()
                );
            }
        }

        Order order =
                Order.builder()
                        .user(user)
                        .status(
                                OrderStatus.CONFIRMED
                        )
                        .totalAmount(
                                BigDecimal.ZERO
                        )
                        .build();

        order =
                orderRepository.save(
                        order
                );

        BigDecimal orderTotal =
                BigDecimal.ZERO;

        List<OrderItemResponse>
                orderItemResponses =
                new ArrayList<>();

        for (CartItem cartItem : cartItems) {

            Product product =
                    cartItem.getProduct();

            Integer quantity =
                    cartItem.getQuantity();

            BigDecimal unitPrice =
                    product.getPrice();

            BigDecimal subtotal =
                    unitPrice.multiply(
                            BigDecimal.valueOf(
                                    quantity
                            )
                    );

            OrderItem orderItem =
                    OrderItem.builder()
                            .order(order)
                            .product(product)
                            .productName(
                                    product.getName()
                            )
                            .unitPrice(
                                    unitPrice
                            )
                            .quantity(
                                    quantity
                            )
                            .subtotal(
                                    subtotal
                            )
                            .build();

            OrderItem savedOrderItem =
                    orderItemRepository
                            .save(orderItem);

            product.setStockQuantity(
                    product.getStockQuantity()
                            - quantity
            );

            productRepository.save(
                    product
            );

            orderTotal =
                    orderTotal.add(
                            subtotal
                    );

            orderItemResponses.add(
                    convertOrderItemToResponse(
                            savedOrderItem
                    )
            );
        }

        order.setTotalAmount(
                orderTotal
        );

        Order savedOrder =
                orderRepository.save(
                        order
                );

        cartItemRepository
                .deleteByUser(user);

        return OrderResponse.builder()
                .orderId(
                        savedOrder.getId()
                )
                .status(
                        savedOrder.getStatus()
                )
                .totalAmount(
                        savedOrder.getTotalAmount()
                )
                .createdAt(
                        savedOrder.getCreatedAt()
                )
                .items(
                        orderItemResponses
                )
                .build();
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> getOrderHistory(
            String email) {

        User user =
                getUserByEmail(email);

        return orderRepository
                .findByUserOrderByCreatedAtDesc(
                        user
                )
                .stream()
                .map(
                        this::
                                convertOrderToResponse
                )
                .toList();
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> getAllOrders() {

        return orderRepository
                .findAllByOrderByCreatedAtDesc()
                .stream()
                .map(
                        this::
                                convertOrderToResponse
                )
                .toList();
    }

    @Transactional
    public OrderResponse updateOrderStatus(
            Long orderId,
            OrderStatus status) {

        Order order =
                orderRepository
                        .findById(orderId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Order not found with id: "
                                                + orderId
                                )
                        );

        order.setStatus(status);

        Order savedOrder =
                orderRepository.save(
                        order
                );

        return convertOrderToResponse(
                savedOrder
        );
    }

    private User getUserByEmail(
            String email) {

        return userRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found with email: "
                                        + email
                        )
                );
    }

    private OrderResponse convertOrderToResponse(
            Order order) {

        List<OrderItemResponse> items =
                orderItemRepository
                        .findByOrderId(
                                order.getId()
                        )
                        .stream()
                        .map(
                                this::
                                        convertOrderItemToResponse
                        )
                        .toList();

        return OrderResponse.builder()
                .orderId(
                        order.getId()
                )
                .status(
                        order.getStatus()
                )
                .totalAmount(
                        order.getTotalAmount()
                )
                .createdAt(
                        order.getCreatedAt()
                )
                .items(items)
                .build();
    }

    private OrderItemResponse
    convertOrderItemToResponse(
            OrderItem orderItem) {

        return OrderItemResponse.builder()
                .orderItemId(
                        orderItem.getId()
                )
                .productId(
                        orderItem
                                .getProduct()
                                .getId()
                )
                .productName(
                        orderItem.getProductName()
                )
                .unitPrice(
                        orderItem.getUnitPrice()
                )
                .quantity(
                        orderItem.getQuantity()
                )
                .subtotal(
                        orderItem.getSubtotal()
                )
                .build();
    }
}