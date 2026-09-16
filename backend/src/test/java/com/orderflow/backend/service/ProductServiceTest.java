package com.orderflow.backend.service;

import com.orderflow.backend.exception.ResourceNotFoundException;
import com.orderflow.backend.model.Product;
import com.orderflow.backend.repository.ProductRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    private Product product;

    @BeforeEach
    void setUp() {

        product = Product.builder()
                .id(1L)
                .name("Samsung Galaxy S25")
                .description("Android smartphone with 256GB storage")
                .price(new BigDecimal("799.99"))
                .stockQuantity(25)
                .category("Electronics")
                .active(true)
                .build();
    }

    @Test
    void createProduct_ShouldSaveAndReturnProduct() {

        // Arrange
        when(productRepository.save(product))
                .thenReturn(product);

        // Act
        Product result = productService.createProduct(product);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Samsung Galaxy S25", result.getName());
        assertEquals(new BigDecimal("799.99"), result.getPrice());
        assertEquals(25, result.getStockQuantity());
        assertEquals("Electronics", result.getCategory());
        assertTrue(result.getActive());

        verify(productRepository, times(1))
                .save(product);
    }

    @Test
    void getProductById_WhenProductExists_ShouldReturnProduct() {

        // Arrange
        when(productRepository.findById(1L))
                .thenReturn(Optional.of(product));

        // Act
        Product result = productService.getProductById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Samsung Galaxy S25", result.getName());
        assertEquals(new BigDecimal("799.99"), result.getPrice());

        verify(productRepository, times(1))
                .findById(1L);
    }

    @Test
    void getProductById_WhenProductDoesNotExist_ShouldThrowException() {

        // Arrange
        when(productRepository.findById(99L))
                .thenReturn(Optional.empty());

        // Act + Assert
        ResourceNotFoundException exception =
                assertThrows(
                        ResourceNotFoundException.class,
                        () -> productService.getProductById(99L)
                );

        assertEquals(
                "Product not found with id: 99",
                exception.getMessage()
        );

        verify(productRepository, times(1))
                .findById(99L);
    }
    @Test
void updateProduct_WhenProductExists_ShouldUpdateAndReturnProduct() {

    // Arrange
    Product updatedProduct = Product.builder()
            .name("Samsung Galaxy S25 Ultra")
            .description("Updated smartphone")
            .price(new BigDecimal("999.99"))
            .stockQuantity(15)
            .category("Electronics")
            .active(true)
            .build();

    when(productRepository.findById(1L))
            .thenReturn(Optional.of(product));

    when(productRepository.save(product))
            .thenReturn(product);

    // Act
    Product result = productService.updateProduct(1L, updatedProduct);

    // Assert
    assertNotNull(result);
    assertEquals("Samsung Galaxy S25 Ultra", result.getName());
    assertEquals("Updated smartphone", result.getDescription());
    assertEquals(new BigDecimal("999.99"), result.getPrice());
    assertEquals(15, result.getStockQuantity());
    assertEquals("Electronics", result.getCategory());
    assertTrue(result.getActive());

    verify(productRepository, times(1)).findById(1L);
    verify(productRepository, times(1)).save(product);
}


@Test
void deleteProduct_WhenProductExists_ShouldDeleteProduct() {

    // Arrange
    when(productRepository.findById(1L))
            .thenReturn(Optional.of(product));

    // Act
    productService.deleteProduct(1L);

    // Assert
    verify(productRepository, times(1)).findById(1L);
    verify(productRepository, times(1)).delete(product);
}
@Test
void updateProduct_WhenProductDoesNotExist_ShouldThrowException() {

    // Arrange
    Product updatedProduct = Product.builder()
            .name("Updated Product")
            .description("Updated description")
            .price(new BigDecimal("499.99"))
            .stockQuantity(10)
            .category("Electronics")
            .active(true)
            .build();

    when(productRepository.findById(99L))
            .thenReturn(Optional.empty());

    // Act + Assert
    ResourceNotFoundException exception =
            assertThrows(
                    ResourceNotFoundException.class,
                    () -> productService.updateProduct(99L, updatedProduct)
            );

    assertEquals(
            "Product not found with id: 99",
            exception.getMessage()
    );

    verify(productRepository, times(1)).findById(99L);

    // save() must never be called
    verify(productRepository, never()).save(any(Product.class));
}


@Test
void deleteProduct_WhenProductDoesNotExist_ShouldThrowException() {

    // Arrange
    when(productRepository.findById(99L))
            .thenReturn(Optional.empty());

    // Act + Assert
    ResourceNotFoundException exception =
            assertThrows(
                    ResourceNotFoundException.class,
                    () -> productService.deleteProduct(99L)
            );

    assertEquals(
            "Product not found with id: 99",
            exception.getMessage()
    );

    verify(productRepository, times(1)).findById(99L);

    // delete() must never be called
    verify(productRepository, never()).delete(any(Product.class));
}
}