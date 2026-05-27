package com.harikrish.ecommerce_api.model.dto.response;

import com.harikrish.ecommerce_api.model.WishListItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WishlistResponse {
        private List<WishListItemResponse> wishListItem;
}
