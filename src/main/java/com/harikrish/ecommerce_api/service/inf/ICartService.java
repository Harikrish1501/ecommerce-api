package com.harikrish.ecommerce_api.service.inf;

import com.harikrish.ecommerce_api.model.CartItem;
import com.harikrish.ecommerce_api.model.dto.request.CartItemRequest;
import com.harikrish.ecommerce_api.model.dto.response.CartResponse;

public interface ICartService {
    String addToCart(CartItemRequest request);
    CartResponse getMyCart();
    CartResponse updateCart(CartItemRequest request);
    CartResponse removeFromCart(Long cartItemId);

}
