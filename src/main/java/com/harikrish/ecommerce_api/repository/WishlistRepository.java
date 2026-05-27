package com.harikrish.ecommerce_api.repository;

import com.harikrish.ecommerce_api.model.WishList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Repository
public interface WishlistRepository extends JpaRepository<WishList, Long> {
    

    Optional<WishList> findByUserId(Long id);
}

