package com.orderflow.backend.config;

import com.orderflow.backend.model.Role;
import com.orderflow.backend.model.User;
import com.orderflow.backend.service.JwtService;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class SecurityIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtService jwtService;


    // =========================================================
    // TEST 1
    // No JWT -> GET products -> 401 Unauthorized
    // =========================================================

    @Test
    void getProducts_WithoutAuthentication_ShouldReturn401()
            throws Exception {

        mockMvc.perform(
                        get("/api/products")
                )
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value(401))
                .andExpect(jsonPath("$.error")
                        .value("Unauthorized"))
                .andExpect(jsonPath("$.message")
                        .value("Authentication is required"));
    }


    // =========================================================
    // TEST 2
    // CUSTOMER JWT -> GET products -> 200 OK
    // =========================================================

    @Test
    void getProducts_WithCustomerToken_ShouldReturn200()
            throws Exception {

        User customer = User.builder()
                .id(1L)
                .name("Test Customer")
                .email("customer@orderflow.com")
                .role(Role.CUSTOMER)
                .build();

        String token = jwtService.generateToken(customer);

        mockMvc.perform(
                        get("/api/products")
                                .header(
                                        "Authorization",
                                        "Bearer " + token
                                )
                )
                .andExpect(status().isOk());
    }


    // =========================================================
    // TEST 3
    // CUSTOMER JWT -> POST product -> 403 Forbidden
    // =========================================================

    @Test
    void createProduct_WithCustomerToken_ShouldReturn403()
            throws Exception {

        User customer = User.builder()
                .id(1L)
                .name("Test Customer")
                .email("customer@orderflow.com")
                .role(Role.CUSTOMER)
                .build();

        String token = jwtService.generateToken(customer);

        String productJson = """
                {
                    "name": "Security Test Laptop",
                    "description": "Testing CUSTOMER authorization",
                    "price": 999.99,
                    "stockQuantity": 10,
                    "category": "Electronics",
                    "active": true
                }
                """;

        mockMvc.perform(
                        post("/api/products")
                                .header(
                                        "Authorization",
                                        "Bearer " + token
                                )
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(productJson)
                )
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.status").value(403))
                .andExpect(jsonPath("$.error")
                        .value("Forbidden"))
                .andExpect(jsonPath("$.message")
                        .value(
                                "You do not have permission to access this resource"
                        ));
    }


    // =========================================================
    // TEST 4
    // ADMIN JWT -> POST product -> 201 Created
    // =========================================================

    @Test
    void createProduct_WithAdminToken_ShouldReturn201()
            throws Exception {

        User admin = User.builder()
                .id(2L)
                .name("OrderFlow Admin")
                .email("admin@orderflow.com")
                .role(Role.ADMIN)
                .build();

        String token = jwtService.generateToken(admin);

        String productJson = """
                {
                    "name": "Admin Security Test Product",
                    "description": "Product created by ADMIN security test",
                    "price": 1499.99,
                    "stockQuantity": 20,
                    "category": "Electronics",
                    "active": true
                }
                """;

        mockMvc.perform(
                        post("/api/products")
                                .header(
                                        "Authorization",
                                        "Bearer " + token
                                )
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(productJson)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name")
                        .value("Admin Security Test Product"))
                .andExpect(jsonPath("$.description")
                        .value(
                                "Product created by ADMIN security test"
                        ))
                .andExpect(jsonPath("$.price")
                        .value(1499.99))
                .andExpect(jsonPath("$.stockQuantity")
                        .value(20))
                .andExpect(jsonPath("$.category")
                        .value("Electronics"))
                .andExpect(jsonPath("$.active")
                        .value(true));
    }
}