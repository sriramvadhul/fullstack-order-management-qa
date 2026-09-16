package com.orderflow.backend.controller;

import com.orderflow.backend.model.Product;
import com.orderflow.backend.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // CREATE
    @PostMapping
    public ResponseEntity<Product> createProduct(
            @Valid @RequestBody Product product) {

        Product createdProduct = productService.createProduct(product);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdProduct);
    }

    // READ ALL
    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {

        return ResponseEntity.ok(
                productService.getAllProducts()
        );
    }

    // READ BY ID
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                productService.getProductById(id)
        );
    }

    // SEARCH BY NAME
    @GetMapping("/search")
    public ResponseEntity<List<Product>> searchProducts(
            @RequestParam String name) {

        return ResponseEntity.ok(
                productService.searchProducts(name)
        );
    }

    // FILTER BY CATEGORY
    @GetMapping("/category/{category}")
    public ResponseEntity<List<Product>> getProductsByCategory(
            @PathVariable String category) {

        return ResponseEntity.ok(
                productService.getProductsByCategory(category)
        );
    }

    // ACTIVE PRODUCTS
    @GetMapping("/active")
    public ResponseEntity<List<Product>> getActiveProducts() {

        return ResponseEntity.ok(
                productService.getActiveProducts()
        );
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody Product product) {

        return ResponseEntity.ok(
                productService.updateProduct(id, product)
        );
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(
            @PathVariable Long id) {

        productService.deleteProduct(id);

        return ResponseEntity.noContent().build();
    }
}