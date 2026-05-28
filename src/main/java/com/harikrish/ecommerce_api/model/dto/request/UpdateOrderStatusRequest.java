package com.harikrish.ecommerce_api.model.dto.request;

import com.harikrish.ecommerce_api.model.enums.OrderStatus;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateOrderStatusRequest {
    @NotNull(message = "Order ID cannot be null")
    private Long id;

    @NotNull(message = "Order status cannot be null")
    private OrderStatus status;
}
