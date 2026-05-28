package com.harikrish.ecommerce_api.service.inf;

import com.harikrish.ecommerce_api.model.dto.request.OrderShippingAddressRequest;
import com.harikrish.ecommerce_api.model.dto.request.UpdateOrderStatusRequest;
import com.harikrish.ecommerce_api.model.dto.response.OrderResponse;

import java.util.List;

public interface IOrderService {
    OrderResponse createOrder(OrderShippingAddressRequest request);
    List<OrderResponse> getMyOrders();
    OrderResponse getMyOrderById(Long id);
    OrderResponse updateOrderStatus(UpdateOrderStatusRequest request);

}
