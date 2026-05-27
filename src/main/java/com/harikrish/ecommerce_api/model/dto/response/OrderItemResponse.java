package com.harikrish.ecommerce_api.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderItemResponse {
    private Long id;
    private Integer quantity;
    private Double priceAtPurchase;
    private Long productId;
    private String productTitle;
    private String productModel;
    private String productBrand;
    private Double subTotal;
    private String productImageUrl;
}
