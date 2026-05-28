package com.harikrish.ecommerce_api.controller;

import com.harikrish.ecommerce_api.model.dto.request.CartItemRequest;
import com.harikrish.ecommerce_api.model.dto.response.CartResponse;
import com.harikrish.ecommerce_api.service.inf.ICartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cart")
public class CartController {
    private final ICartService cartService;

    @PostMapping("")
    public ResponseEntity<String> addToCart(@Valid @RequestBody CartItemRequest request){
        String response = cartService.addToCart(request);
        return ResponseEntity.ok(response);
    }
    @PutMapping("")
    public ResponseEntity<CartResponse> updateCart(@Valid @RequestBody CartItemRequest request){
        CartResponse response = cartService.updateCart(request);
        return ResponseEntity.ok(response);
    }
    @DeleteMapping("/{cartItemId}")
    public ResponseEntity<CartResponse> deleteCart(@PathVariable Long productid){
        CartResponse response = cartService.removeFromCart(productid);
        return ResponseEntity.ok(response);

    }

    @GetMapping("")
    public ResponseEntity<CartResponse> getMyCart(){
        CartResponse response = cartService.getMyCart();
        return ResponseEntity.ok(response);
    }
}
