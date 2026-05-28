package com.harikrish.ecommerce_api.repository;

import com.harikrish.ecommerce_api.model.WishListItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WishlistItemRepository extends JpaRepository<WishListItem , Long> {
}
