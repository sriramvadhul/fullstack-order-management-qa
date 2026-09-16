package com.orderflow.backend.service;

import com.orderflow.backend.model.Product;
import com.orderflow.backend.repository.ProductRepository;
import org.springframework.stereotype.Service;
import com.orderflow.backend.exception.ResourceNotFoundException;
import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    // Create product
    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    // Get all products
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    // Get product by ID
  public Product getProductById(Long id) {
    return productRepository.findById(id)
            .orElseThrow(() ->
                    new ResourceNotFoundException(
                            "Product not found with id: " + id
                    ));
}

    // Search products by name
    public List<Product> searchProducts(String name) {
        return productRepository.findByNameContainingIgnoreCase(name);
    }

    // Get products by category
    public List<Product> getProductsByCategory(String category) {
        return productRepository.findByCategoryIgnoreCase(category);
    }

    // Get active products
    public List<Product> getActiveProducts() {
        return productRepository.findByActiveTrue();
    }

    // Update product
    public Product updateProduct(Long id, Product updatedProduct) {

        Product existingProduct = getProductById(id);

        existingProduct.setName(updatedProduct.getName());
        existingProduct.setDescription(updatedProduct.getDescription());
        existingProduct.setPrice(updatedProduct.getPrice());
        existingProduct.setStockQuantity(updatedProduct.getStockQuantity());
        existingProduct.setCategory(updatedProduct.getCategory());
        existingProduct.setActive(updatedProduct.getActive());

        return productRepository.save(existingProduct);
    }

    // Delete product
    public void deleteProduct(Long id) {

        Product existingProduct = getProductById(id);

        productRepository.delete(existingProduct);
    }
}