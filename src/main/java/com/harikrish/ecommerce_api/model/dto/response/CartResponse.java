package com.harikrish.ecommerce_api.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartResponse {
    private Long id;
    private List<CartItemResponse> cartItemResponseList;
    private Double totalPrice;
}
