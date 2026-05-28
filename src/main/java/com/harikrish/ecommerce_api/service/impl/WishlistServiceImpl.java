package com.harikrish.ecommerce_api.service.impl;

import com.harikrish.ecommerce_api.exception.BadRequestException;
import com.harikrish.ecommerce_api.exception.ResourceNotFound;
import com.harikrish.ecommerce_api.model.Product;
import com.harikrish.ecommerce_api.model.User;
import com.harikrish.ecommerce_api.model.WishList;
import com.harikrish.ecommerce_api.model.WishListItem;
import com.harikrish.ecommerce_api.model.dto.request.WishlistItemRequest;
import com.harikrish.ecommerce_api.model.dto.response.ProductResponse;
import com.harikrish.ecommerce_api.model.dto.response.WishListItemResponse;
import com.harikrish.ecommerce_api.model.dto.response.WishlistResponse;
import com.harikrish.ecommerce_api.repository.ProductRepository;
import com.harikrish.ecommerce_api.repository.WishlistItemRepository;
import com.harikrish.ecommerce_api.repository.WishlistRepository;
import com.harikrish.ecommerce_api.service.inf.IUserService;
import com.harikrish.ecommerce_api.service.inf.IWishlist;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WishlistServiceImpl implements IWishlist {

    private final IUserService userService;
    private final WishlistRepository wishlistRepository;
    private final ProductRepository productRepository;
    private final WishlistItemRepository wishlistItemRepository;
    @Override
    @Transactional
    public String addToWishlist(WishlistItemRequest request) {
        User currentUser = userService.getCurrentUser();

        WishList wishList = wishlistRepository.findByUserId(currentUser.getId())
                .orElseGet(() -> createWishlistForUser(currentUser));

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ResourceNotFound("Product not found with id " + request.getProductId()));

        boolean isAlreadyExited = wishList.getWishListItem().stream()
                .anyMatch(item -> item.getProduct().getId().equals(product.getId()));
        if (isAlreadyExited) {
            throw new BadRequestException("Product is already in the wishlist");
        }
        WishListItem wishListItem = WishListItem.builder()
                .wishlist(wishList)
                .product(product)
                .build();

        wishList.getWishListItem().add(wishListItem);
        wishlistRepository.save(wishList);
        return "Product added to wishlist successfully";
    }
    @Override
    public WishlistResponse getWishlist() {
        User currentUser = userService.getCurrentUser();
        WishList wishList = wishlistRepository.findByUserId(currentUser.getId())
                .orElseGet(()->createWishlistForUser(currentUser));
        return mapToWishlistResponse(wishList);

    }

    private WishList createWishlistForUser(User user) {
        WishList wishList = WishList.builder()
                .user(user)
                .build();
        return wishlistRepository.save(wishList);
    }
    private WishlistResponse  mapToWishlistResponse(WishList wishList){
        List<WishListItemResponse> items = wishList.getWishListItem().stream()
                .map(this::mapToWishlistItemResponse)
                .toList();
        return WishlistResponse.builder()
                .wishListItem(items)
                .build();
    }
    private WishListItemResponse mapToWishlistItemResponse(WishListItem item){
        return WishListItemResponse.builder()
                .id(item.getId())
                .product(mapToProductResponse(item.getProduct()))
                .build();
    }
    private ProductResponse mapToProductResponse(Product product){
        return ProductResponse.builder()
                .id(product.getId())
                .title(product.getTitle())
                .brand(product.getBrand())
                .model(product.getModel())
                .description(product.getDescription())
                .price(product.getPrice())
                .stockQuantity(product.getQuantity())
                .category(product.getCategory())
                .imageUrl(product.getImageUrl())
                .sellerId(product.getSeller().getId())
                .sellerName(product.getSeller().getName())
                .build();

    }
    @Override
    @Transactional
    public WishlistResponse removeFromWishlist(Long wishlistItemId) {
        User currentUser = userService.getCurrentUser();
        WishList wishList = wishlistRepository.findByUserId(currentUser.getId())
                .orElseThrow(()-> new ResourceNotFound("Wishlist not found for user id "+currentUser.getId()));

        WishListItem wishListItem = wishlistItemRepository.findById(wishlistItemId)
                .orElseThrow(()-> new ResourceNotFound("Wishlist item not found with id "+wishlistItemId));

        wishList.getWishListItem().remove(wishListItem);
        wishlistItemRepository.delete(wishListItem);
        wishlistRepository.save(wishList);
        return mapToWishlistResponse(wishList);
    }
}
