package com.harikrish.ecommerce_api.service.impl;

import com.harikrish.ecommerce_api.exception.ResourceNotFound;
import com.harikrish.ecommerce_api.exception.UnauthorizedException;
import com.harikrish.ecommerce_api.model.Product;
import com.harikrish.ecommerce_api.model.User;
import com.harikrish.ecommerce_api.model.dto.request.ProductRequest;
import com.harikrish.ecommerce_api.model.dto.response.ProductResponse;
import com.harikrish.ecommerce_api.model.enums.UserRole;
import com.harikrish.ecommerce_api.repository.ProductRepository;

import com.harikrish.ecommerce_api.service.inf.IProductService;
import com.harikrish.ecommerce_api.service.inf.IUserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements IProductService {

    private final ProductRepository productRepository;
    private final IUserService userService;

    @Override
    public List<ProductResponse> getAllProducts() {
        return productRepository.findAll().stream()
                .map(this::mapToProductResponse)
                .toList();
    }

    @Override
    public ProductResponse getProductById(Long id) {
        return   mapToProductResponse(productRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFound("Product ID is not found"+id))) ;
    }

    @Override
    public List<ProductResponse> getProductsByCategory(String category) {
        return productRepository.findByCategory(category).stream()
                .map(this::mapToProductResponse)
                .toList();
    }

    @Override
    public List<ProductResponse> getProductsByPriceRange(Double minPrice, Double maxPrice) {
        return productRepository.findByPriceRange(minPrice,maxPrice).stream()
                .map(this::mapToProductResponse)
                .toList();
    }

    @Override
    public List<ProductResponse> searchProducts(String keyword) {
        return productRepository
                .findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(keyword,keyword).stream()
                .map(this::mapToProductResponse)
                .toList();
    }

    @Override
    @Transactional

    public String createProduct(ProductRequest request) {
        User currentUser = userService.getCurrentUser();
        if(currentUser.getRole() != UserRole.SELLER){
            throw new UnauthorizedException("Only sellers can create products");
        }
        Product product = Product.builder()
                .title(request.getTitle())
                .brand(request.getBrand())
                .model(request.getModel())
                .category(request.getCategory())
                .description(request.getDescription())
                .quantity(request.getStockQuantity())
                .price(request.getPrice())
                .imageUrl(request.getImageUrl())
                .seller(currentUser)
                .build();
        productRepository.save(product);


        return "Successfully created product with ID: "+product.getId();
    }

    @Override
    @Transactional
    @Modifying
    public String updateProduct(Long id, ProductRequest request) {
        Product product = productRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFound("Product ID is not found"+id));
        User currentUser = userService.getCurrentUser();
        if(!product.getSeller().getId().equals(currentUser.getId()) && currentUser.getRole()!= UserRole.ADMIN){
            throw new UnauthorizedException("You are not authorized to update this product");
        }
        product.setTitle(request.getTitle());
        product.setBrand(request.getBrand());
        product.setModel(request.getModel());
        product.setCategory(request.getCategory());
        product.setDescription(request.getDescription());
        product.setQuantity(request.getStockQuantity());
        product.setPrice(request.getPrice());
        product.setImageUrl(request.getImageUrl());
        productRepository.save(product);
        return "Successfully updated product with ID: "+product.getId();
    }

    @Override
    @Transactional
    public String deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFound("Product ID is not found"+id));
        User currentUser = userService.getCurrentUser();
        if(!product.getSeller().getId().equals(currentUser.getId()) && currentUser.getRole()!= UserRole.ADMIN){
            throw new UnauthorizedException("You are not authorized to delete this product");
        }
        productRepository.delete(product);
        return "Successfully Deleted product ";
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
}
