package com.orderflow.backend.service;

import com.orderflow.backend.dto.AddToCartRequest;
import com.orderflow.backend.dto.CartItemResponse;
import com.orderflow.backend.dto.UpdateCartItemRequest;
import com.orderflow.backend.exception.ResourceNotFoundException;
import com.orderflow.backend.model.CartItem;
import com.orderflow.backend.model.Product;
import com.orderflow.backend.model.User;
import com.orderflow.backend.repository.CartItemRepository;
import com.orderflow.backend.repository.ProductRepository;
import com.orderflow.backend.repository.UserRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class CartService {

    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public CartService(
            CartItemRepository cartItemRepository,
            ProductRepository productRepository,
            UserRepository userRepository) {

        this.cartItemRepository = cartItemRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public CartItemResponse addToCart(
            String email,
            AddToCartRequest request) {

        User user = getUserByEmail(email);

        Product product = productRepository
                .findById(request.getProductId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Product not found with id: "
                                        + request.getProductId()
                        )
                );

        if (!Boolean.TRUE.equals(product.getActive())) {
            throw new IllegalArgumentException(
                    "Product is not available"
            );
        }

        CartItem cartItem = cartItemRepository
                .findByUserAndProductId(
                        user,
                        product.getId()
                )
                .orElse(null);

        int requestedQuantity =
                request.getQuantity();

        int newQuantity;

        if (cartItem == null) {
            newQuantity = requestedQuantity;
        } else {
            newQuantity =
                    cartItem.getQuantity()
                            + requestedQuantity;
        }

        if (newQuantity > product.getStockQuantity()) {
            throw new IllegalArgumentException(
                    "Requested quantity exceeds available stock"
            );
        }

        if (cartItem == null) {

            cartItem = CartItem.builder()
                    .user(user)
                    .product(product)
                    .quantity(requestedQuantity)
                    .build();

        } else {

            cartItem.setQuantity(newQuantity);
        }

        CartItem savedCartItem =
                cartItemRepository.save(cartItem);

        return convertToResponse(savedCartItem);
    }

    @Transactional(readOnly = true)
    public List<CartItemResponse> getCart(
            String email) {

        User user = getUserByEmail(email);

        return cartItemRepository
                .findByUser(user)
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    @Transactional
    public CartItemResponse updateCartItem(
            String email,
            Long cartItemId,
            UpdateCartItemRequest request) {

        User user = getUserByEmail(email);

        CartItem cartItem =
                getCartItemForUser(
                        cartItemId,
                        user
                );

        Product product =
                cartItem.getProduct();

        if (request.getQuantity()
                > product.getStockQuantity()) {

            throw new IllegalArgumentException(
                    "Requested quantity exceeds available stock"
            );
        }

        cartItem.setQuantity(
                request.getQuantity()
        );

        CartItem savedCartItem =
                cartItemRepository.save(cartItem);

        return convertToResponse(savedCartItem);
    }

    @Transactional
    public void removeCartItem(
            String email,
            Long cartItemId) {

        User user = getUserByEmail(email);

        CartItem cartItem =
                getCartItemForUser(
                        cartItemId,
                        user
                );

        cartItemRepository.delete(cartItem);
    }

    @Transactional
    public void clearCart(
            String email) {

        User user = getUserByEmail(email);

        cartItemRepository.deleteByUser(user);
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

    private CartItem getCartItemForUser(
            Long cartItemId,
            User user) {

        return cartItemRepository
                .findByIdAndUser(
                        cartItemId,
                        user
                )
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Cart item not found with id: "
                                        + cartItemId
                        )
                );
    }

    private CartItemResponse convertToResponse(
            CartItem cartItem) {

        Product product =
                cartItem.getProduct();

        BigDecimal subtotal =
                product.getPrice().multiply(
                        BigDecimal.valueOf(
                                cartItem.getQuantity()
                        )
                );

        return CartItemResponse.builder()
                .cartItemId(cartItem.getId())
                .productId(product.getId())
                .productName(product.getName())
                .price(product.getPrice())
                .quantity(cartItem.getQuantity())
                .subtotal(subtotal)
                .build();
    }
}