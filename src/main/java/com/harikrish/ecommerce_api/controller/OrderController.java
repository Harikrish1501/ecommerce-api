package com.harikrish.ecommerce_api.controller;

import com.harikrish.ecommerce_api.model.dto.request.OrderShippingAddressRequest;
import com.harikrish.ecommerce_api.model.dto.request.UpdateOrderStatusRequest;
import com.harikrish.ecommerce_api.model.dto.response.OrderResponse;
import com.harikrish.ecommerce_api.service.inf.IOrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class OrderController {
    private final IOrderService orderService;

    @PostMapping("/order")
    public ResponseEntity<OrderResponse> createOrder(@Valid @RequestBody OrderShippingAddressRequest request){
        OrderResponse response = orderService.createOrder(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    @GetMapping("/order/{orderId}")
    public ResponseEntity<OrderResponse> getOrderById(@PathVariable Long orderId){
        OrderResponse response = orderService.getMyOrderById(orderId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/orders")
    public ResponseEntity<List<OrderResponse>> getMyOrders(){
        List<OrderResponse> responses = orderService.getMyOrders();
        return ResponseEntity.ok(responses);
    }

    @PutMapping("/order")
    public ResponseEntity <OrderResponse> updateOrderStatus(@Valid @RequestBody UpdateOrderStatusRequest request){
        OrderResponse response = orderService.updateOrderStatus(request);
        return ResponseEntity.ok(response);
    }

}
