package com.orderflow.backend.repository;

import com.orderflow.backend.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByActiveTrue();

    List<Product> findByCategoryIgnoreCase(String category);

    List<Product> findByNameContainingIgnoreCase(String name);
}