package com.harikrish.ecommerce_api.service.inf;

import com.harikrish.ecommerce_api.model.dto.request.WishlistItemRequest;
import com.harikrish.ecommerce_api.model.dto.response.WishlistResponse;

public interface IWishlist {
    String addToWishlist(WishlistItemRequest request);
    WishlistResponse getWishlist();
    WishlistResponse removeFromWishlist(Long  wishlistItemId);
}
