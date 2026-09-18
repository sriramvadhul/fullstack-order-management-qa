package com.orderflow.backend.controller;

import com.orderflow.backend.dto.AddToCartRequest;
import com.orderflow.backend.dto.CartItemResponse;
import com.orderflow.backend.dto.UpdateCartItemRequest;
import com.orderflow.backend.service.CartService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;

    public CartController(
            CartService cartService) {

        this.cartService = cartService;
    }

    // Add product to cart
    @PostMapping
    public ResponseEntity<CartItemResponse> addToCart(
            @Valid @RequestBody AddToCartRequest request,
            Authentication authentication) {

        String email =
                authentication.getName();

        CartItemResponse cartItem =
                cartService.addToCart(
                        email,
                        request
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(cartItem);
    }

    // Get logged-in user's cart
    @GetMapping
    public ResponseEntity<List<CartItemResponse>> getCart(
            Authentication authentication) {

        String email =
                authentication.getName();

        List<CartItemResponse> cart =
                cartService.getCart(email);

        return ResponseEntity.ok(cart);
    }

    // Update quantity
    @PutMapping("/{cartItemId}")
    public ResponseEntity<CartItemResponse> updateCartItem(
            @PathVariable Long cartItemId,
            @Valid @RequestBody UpdateCartItemRequest request,
            Authentication authentication) {

        String email =
                authentication.getName();

        CartItemResponse cartItem =
                cartService.updateCartItem(
                        email,
                        cartItemId,
                        request
                );

        return ResponseEntity.ok(cartItem);
    }

    // Remove one item
    @DeleteMapping("/{cartItemId}")
    public ResponseEntity<Void> removeCartItem(
            @PathVariable Long cartItemId,
            Authentication authentication) {

        String email =
                authentication.getName();

        cartService.removeCartItem(
                email,
                cartItemId
        );

        return ResponseEntity.noContent().build();
    }

    // Clear entire cart
    @DeleteMapping
    public ResponseEntity<Void> clearCart(
            Authentication authentication) {

        String email =
                authentication.getName();

        cartService.clearCart(email);

        return ResponseEntity.noContent().build();
    }
}