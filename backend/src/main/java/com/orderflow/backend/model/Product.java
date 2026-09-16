package com.orderflow.backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Product name is required")
    @Column(nullable = false)
    private String name;

    @Column(length = 1000)
    private String description;

    @NotNull(message = "Price is required")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @NotNull(message = "Stock quantity is required")
    @Min(value = 0, message = "Stock cannot be negative")
    @Column(nullable = false)
    private Integer stockQuantity;

    @NotBlank(message = "Category is required")
    @Column(nullable = false)
    private String category;
    
    @Builder.Default
    @Column(nullable = false)
    private Boolean active = true;
}