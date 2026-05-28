package com.harikrish.ecommerce_api.repository;

import com.harikrish.ecommerce_api.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    Optional<CartItem>  findByCartIdAndProductId(Long id, Long id1);
}
