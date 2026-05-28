package com.harikrish.ecommerce_api.controller;


import com.harikrish.ecommerce_api.model.dto.request.WishlistItemRequest;
import com.harikrish.ecommerce_api.model.dto.response.WishlistResponse;
import com.harikrish.ecommerce_api.service.inf.IWishlist;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class WishlistController {

    private final IWishlist wishlist;

    @PostMapping("/wishlist")
    public ResponseEntity<String> addToWishlist(@Valid @RequestBody WishlistItemRequest request) {
        String response = wishlist.addToWishlist(request);
        return ResponseEntity.ok(response);
    }
    @DeleteMapping("/wishlist/{wishlistItemId}")
    public ResponseEntity<WishlistResponse> removeFromWishlist(@PathVariable Long wishlistItemId) {
        WishlistResponse response = wishlist.removeFromWishlist(wishlistItemId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/wishlist")
    public ResponseEntity<WishlistResponse> getWishlist() {
        WishlistResponse response = wishlist.getWishlist();
        return ResponseEntity.ok(response);
    }

}
