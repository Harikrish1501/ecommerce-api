package com.harikrish.ecommerce_api.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderShippingAddressRequest {

    @NotBlank(message = "Shipping address is required")
    private String shippingAddress;

}
