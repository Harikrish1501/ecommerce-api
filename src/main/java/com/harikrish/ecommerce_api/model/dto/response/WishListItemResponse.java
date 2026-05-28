package com.harikrish.ecommerce_api.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WishListItemResponse{
    private Long id;
    private ProductResponse product;

}
