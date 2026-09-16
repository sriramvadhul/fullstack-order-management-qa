package com.orderflow.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.orderflow.backend.exception.GlobalExceptionHandler;
import com.orderflow.backend.exception.ResourceNotFoundException;
import com.orderflow.backend.model.Product;
import com.orderflow.backend.service.ProductService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;


import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class ProductControllerTest {

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @Mock
    private ProductService productService;

  
    private Product product;

    @BeforeEach
void setUp() {

    ProductController productController =
            new ProductController(productService);

    mockMvc = MockMvcBuilders
            .standaloneSetup(productController)
            .setControllerAdvice(new GlobalExceptionHandler())
            .build();

    objectMapper = new ObjectMapper();

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
    void createProduct_WithValidData_ShouldReturn201() throws Exception {

        when(productService.createProduct(any(Product.class)))
                .thenReturn(product);

        mockMvc.perform(
                        post("/api/products")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(product))
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name")
                        .value("Samsung Galaxy S25"))
                .andExpect(jsonPath("$.price").value(799.99))
                .andExpect(jsonPath("$.stockQuantity").value(25))
                .andExpect(jsonPath("$.category")
                        .value("Electronics"))
                .andExpect(jsonPath("$.active").value(true));
    }

    @Test
    void getAllProducts_ShouldReturn200AndProductList() throws Exception {

        when(productService.getAllProducts())
                .thenReturn(List.of(product));

        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name")
                        .value("Samsung Galaxy S25"))
                .andExpect(jsonPath("$[0].price").value(799.99))
                .andExpect(jsonPath("$[0].stockQuantity").value(25))
                .andExpect(jsonPath("$[0].category")
                        .value("Electronics"))
                .andExpect(jsonPath("$[0].active").value(true));
    }

    @Test
    void getProductById_WhenProductExists_ShouldReturn200()
            throws Exception {

        when(productService.getProductById(1L))
                .thenReturn(product);

        mockMvc.perform(get("/api/products/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name")
                        .value("Samsung Galaxy S25"))
                .andExpect(jsonPath("$.price").value(799.99))
                .andExpect(jsonPath("$.stockQuantity").value(25))
                .andExpect(jsonPath("$.category")
                        .value("Electronics"))
                .andExpect(jsonPath("$.active").value(true));
    }

    @Test
    void getProductById_WhenProductDoesNotExist_ShouldReturn404()
            throws Exception {

        when(productService.getProductById(99L))
                .thenThrow(
                        new ResourceNotFoundException(
                                "Product not found with id: 99"
                        )
                );

        mockMvc.perform(get("/api/products/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message")
                        .value("Product not found with id: 99"));
    }

    @Test
    void createProduct_WithInvalidData_ShouldReturn400()
            throws Exception {

        String invalidProductJson = """
                {
                    "description": "Invalid test product",
                    "stockQuantity": -5,
                    "category": "",
                    "active": true
                }
                """;

        mockMvc.perform(
                        post("/api/products")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(invalidProductJson)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error")
                        .value("Validation Failed"))
                .andExpect(jsonPath("$.errors.name")
                        .value("Product name is required"))
                .andExpect(jsonPath("$.errors.price")
                        .value("Price is required"))
                .andExpect(jsonPath("$.errors.stockQuantity")
                        .value("Stock cannot be negative"))
                .andExpect(jsonPath("$.errors.category")
                        .value("Category is required"));
    }
@Test
void updateProduct_WhenProductExists_ShouldReturn200()
        throws Exception {

    Product updatedProduct = Product.builder()
            .id(1L)
            .name("Samsung Galaxy S25 Ultra")
            .description("Updated flagship smartphone")
            .price(new BigDecimal("999.99"))
            .stockQuantity(20)
            .category("Electronics")
            .active(true)
            .build();

    when(productService.updateProduct(
            org.mockito.ArgumentMatchers.eq(1L),
            any(Product.class)
    )).thenReturn(updatedProduct);

    mockMvc.perform(
                    put("/api/products/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(updatedProduct))
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.name")
                    .value("Samsung Galaxy S25 Ultra"))
            .andExpect(jsonPath("$.description")
                    .value("Updated flagship smartphone"))
            .andExpect(jsonPath("$.price").value(999.99))
            .andExpect(jsonPath("$.stockQuantity").value(20))
            .andExpect(jsonPath("$.category")
                    .value("Electronics"))
            .andExpect(jsonPath("$.active").value(true));
}


@Test
void deleteProduct_WhenProductExists_ShouldReturn204()
        throws Exception {

    mockMvc.perform(
                    delete("/api/products/1")
            )
            .andExpect(status().isNoContent());
}    
@Test
void searchProducts_ShouldReturnMatchingProducts()
        throws Exception {

    when(productService.searchProducts("Samsung"))
            .thenReturn(List.of(product));

    mockMvc.perform(
                    get("/api/products/search")
                            .param("name", "Samsung")
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").value(1))
            .andExpect(jsonPath("$[0].name")
                    .value("Samsung Galaxy S25"))
            .andExpect(jsonPath("$[0].category")
                    .value("Electronics"));
}


@Test
void getProductsByCategory_ShouldReturnMatchingProducts()
        throws Exception {

    when(productService.getProductsByCategory("Electronics"))
            .thenReturn(List.of(product));

    mockMvc.perform(
                    get("/api/products/category/Electronics")
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").value(1))
            .andExpect(jsonPath("$[0].name")
                    .value("Samsung Galaxy S25"))
            .andExpect(jsonPath("$[0].category")
                    .value("Electronics"));
}


@Test
void getActiveProducts_ShouldReturnActiveProducts()
        throws Exception {

    when(productService.getActiveProducts())
            .thenReturn(List.of(product));

    mockMvc.perform(
                    get("/api/products/active")
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").value(1))
            .andExpect(jsonPath("$[0].name")
                    .value("Samsung Galaxy S25"))
            .andExpect(jsonPath("$[0].active")
                    .value(true));
}
}