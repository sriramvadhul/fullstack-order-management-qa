package com.orderflow.backend.repository;

import com.orderflow.backend.model.CartItem;
import com.orderflow.backend.model.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemRepository
        extends JpaRepository<CartItem, Long> {

    List<CartItem> findByUser(User user);

    Optional<CartItem> findByUserAndProductId(
            User user,
            Long productId
    );

    Optional<CartItem> findByIdAndUser(
            Long id,
            User user
    );

    void deleteByUser(User user);
}