package com.harikrish.ecommerce_api.service.impl;

import com.harikrish.ecommerce_api.exception.BadRequestException;
import com.harikrish.ecommerce_api.exception.ResourceNotFound;
import com.harikrish.ecommerce_api.model.Cart;
import com.harikrish.ecommerce_api.model.CartItem;
import com.harikrish.ecommerce_api.model.Product;
import com.harikrish.ecommerce_api.model.User;
import com.harikrish.ecommerce_api.model.dto.request.CartItemRequest;
import com.harikrish.ecommerce_api.model.dto.response.CartItemResponse;
import com.harikrish.ecommerce_api.model.dto.response.CartResponse;
import com.harikrish.ecommerce_api.repository.CartItemRepository;
import com.harikrish.ecommerce_api.repository.CartRepository;
import com.harikrish.ecommerce_api.repository.ProductRepository;
import com.harikrish.ecommerce_api.service.inf.ICartService;
import com.harikrish.ecommerce_api.service.inf.IUserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
@Service
@RequiredArgsConstructor
public class CartServiceImpl implements ICartService {

    private final CartRepository cartRepository;
    private final IUserService userService;
    private final ProductRepository productRepository;
    private final CartItemRepository cartItemRepository;

    @Override
    @Transactional
    public String addToCart(CartItemRequest request) {

        User currentUser = userService.getCurrentUser(); // Replace with actual user ID retrieval logic
        Cart cart = cartRepository.findByUserId(currentUser.getId())
                .orElseGet(()->createMyCart(currentUser));
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(()-> new ResourceNotFound("Product ID is not found"));

        if(product.getQuantity() < request.getQuantity()){
            throw new BadRequestException("Requested quantity is not available in stock");
        }

        CartItem cartItem = cartItemRepository.findByCartIdAndProductId(cart.getId(),product.getId()).orElse(null);

        if(cartItem !=null){
            cartItem.setQuantity(cartItem.getQuantity() + request.getQuantity());

        }
        else{
            cartItem = CartItem.builder()
                    .cart(cart)
                    .product(product)
                    .quantity(request.getQuantity())
                    .build();
             cart.addItem(cartItem);
        }
        cartItemRepository.save(cartItem);
        return "Product Added to cart successfully";

    }


    @Override
    public CartResponse getMyCart() {
        User currentUser = userService.getCurrentUser();
        Cart cart = cartRepository.findByUserId(currentUser.getId())
                .orElseGet(()->createMyCart(currentUser));

        return mapToCardResponse(cart);
    }

    private Cart createMyCart(User currentUser) {
        Cart cart = Cart.builder()
                .user(currentUser)
                .build();
        cartRepository.save(cart);
        return cart;
    }
    private CartResponse mapToCardResponse(Cart cart){
        List<CartItem> items = cart.getCartItems();
        List<CartItemResponse> responseItem = cart.getCartItems().stream()
                .map(this::mapToCardItemResponse)
                .toList();
        double totalPrice = 0.0;
        for(CartItem item : items){
            totalPrice+= (item.getProduct().getPrice() * item.getProduct().getQuantity());
        }
        return CartResponse.builder()
                .id(cart.getId())
                .cartItemResponseList(responseItem)
                .totalPrice(totalPrice)
                .build();

    }

    private CartItemResponse mapToCardItemResponse(CartItem cartItem) {
        double subTotal = cartItem.getProduct().getPrice() * cartItem.getProduct().getQuantity();
        return CartItemResponse.builder()
                .id(cartItem.getId())
                .quantity(cartItem.getProduct().getQuantity())
                .productPrice(cartItem.getProduct().getPrice())
                .productModel(cartItem.getProduct().getModel())
                .productId(cartItem.getProduct().getId())
                .productBrand(cartItem.getProduct().getBrand())
                .productTitle(cartItem.getProduct().getTitle())
                .subTotal(subTotal)
                .productImageUrl(cartItem.getProduct().getImageUrl())
                .build();

    }

    @Override
    @Transactional
    public CartResponse updateCart(CartItemRequest request) {
        User currentUser = userService.getCurrentUser(); // Replace with actual user ID retrieval logic
        Cart cart = cartRepository.findByUserId(currentUser.getId())
                . orElseThrow(()-> new ResourceNotFound("Cart is not found in cart"));
        CartItem cartItem = cartItemRepository.findByCartIdAndProductId(cart.getId(),request.getProductId()).
                        orElseThrow(()-> new ResourceNotFound("Product is not found in cart"));
        if(request.getQuantity() == 0){
            cart.removeItem(cartItem);
            cartItemRepository.delete(cartItem);
        }
        else{
            if(cartItem.getProduct().getQuantity() < request.getQuantity()){
                throw new BadRequestException("Requested quantity is not available in stock");
            }
            cartItem.setQuantity(request.getQuantity());
            cartRepository.save(cart);
        }
        return mapToCardResponse(cart);
    }

    @Override
    @Transactional
    public CartResponse removeFromCart(Long cartItemId) {
        User currentUser = userService.getCurrentUser(); // Replace with actual user ID retrieval logic
        Cart cart = cartRepository.findByUserId(currentUser.getId())
                . orElseThrow(()-> new ResourceNotFound("Cart is not found in cart"));
        CartItem cartItem = cartItemRepository.findById(cartItemId).
                orElseThrow(()-> new ResourceNotFound("CartItemID is not found in cart"));

        cart.removeItem(cartItem);
        cartItemRepository.delete(cartItem);
        cartRepository.save(cart);
        return mapToCardResponse(cart);
    }




}
