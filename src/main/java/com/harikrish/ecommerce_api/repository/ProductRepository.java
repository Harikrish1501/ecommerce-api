package com.harikrish.ecommerce_api.repository;

import com.harikrish.ecommerce_api.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByCategory(String category);

    @Query("SELECT p FROM Product p WHERE p.price BETWEEN :minPrice AND :maxPrice")
    List<Product> findByPriceRange(Double minPrice, Double maxPrice);



    List<Product> findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String keyword, String keyword1);
}
