package com.harikrish.ecommerce_api.controller;

import com.harikrish.ecommerce_api.model.dto.request.ProductRequest;
import com.harikrish.ecommerce_api.model.dto.response.ProductResponse;
import com.harikrish.ecommerce_api.service.inf.IProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/productcontroller")
public class ProductController {

    private final IProductService productService;
    @GetMapping("/products")
    public ResponseEntity<List<ProductResponse>> getAllProducts(){
        List<ProductResponse> productResponses = productService.getAllProducts();
        return new ResponseEntity<>(productResponses, HttpStatus.OK);
    }
    @GetMapping("/product/{id}")
    public ResponseEntity<ProductResponse> getProduct(@PathVariable Long id){
        ProductResponse productResponse = productService.getProductById(id);
        return ResponseEntity.ok(productResponse);
    }
    @GetMapping("/product/category/{category}")
    public ResponseEntity< List<ProductResponse>> getProductsByCategory(@PathVariable String category){
        List<ProductResponse> productResponses = productService.getProductsByCategory(category);
        return ResponseEntity.ok(productResponses);
    }
    @GetMapping("/product/price")
    public ResponseEntity<List<ProductResponse>> getProductsByPriceRange(@RequestParam Double minPrice,@RequestParam Double maxPrice){
        List<ProductResponse> productResponses = productService.getProductsByPriceRange(minPrice,maxPrice);
        return ResponseEntity.ok(productResponses);
    }
    @GetMapping("/product/search")
    public ResponseEntity<List<ProductResponse>> searchProducts(@RequestParam String keyword){
        List<ProductResponse> productResponses = productService.searchProducts(keyword);
        return ResponseEntity.ok(productResponses);
    }
    @PostMapping("/product")
    public ResponseEntity<String> createProduct(@Valid @RequestBody ProductRequest request){
        String create = productService.createProduct(request);
        return ResponseEntity.ok(create);
    }
    @PutMapping("/product")
    @PreAuthorize("hasRole('SELLER')") // Only users with the ADMIN role can access this endpoint
    public ResponseEntity<String> updateProduct(@Valid @RequestParam Long id,@Valid  @RequestBody ProductRequest request){
        String response = productService.updateProduct(id,request);
        return ResponseEntity.ok(response);
    }
    @DeleteMapping("/product")
    public ResponseEntity<String> deleteProduct(@RequestParam Long id){
        String delete = productService.deleteProduct(id);
        return ResponseEntity.ok(delete);
    }

}
