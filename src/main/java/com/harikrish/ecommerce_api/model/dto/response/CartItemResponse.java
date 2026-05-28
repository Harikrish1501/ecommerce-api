package com.harikrish.ecommerce_api.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartItemResponse {
    private Long id;
    private Integer quantity;
    private Double productPrice;
    private Long productId;
    private String productTitle;
    private String productModel;
    private String productBrand;
    private String productImageUrl;
    private Double subTotal;
}
