package com.harikrish.ecommerce_api.service.impl;

import com.harikrish.ecommerce_api.exception.BadRequestException;
import com.harikrish.ecommerce_api.exception.ResourceNotFound;
import com.harikrish.ecommerce_api.model.*;
import com.harikrish.ecommerce_api.model.dto.request.OrderShippingAddressRequest;
import com.harikrish.ecommerce_api.model.dto.request.UpdateOrderStatusRequest;
import com.harikrish.ecommerce_api.model.dto.response.OrderItemResponse;
import com.harikrish.ecommerce_api.model.dto.response.OrderResponse;
import com.harikrish.ecommerce_api.model.enums.OrderStatus;
import com.harikrish.ecommerce_api.repository.CartRepository;
import com.harikrish.ecommerce_api.repository.OrderRepository;
import com.harikrish.ecommerce_api.repository.ProductRepository;
import com.harikrish.ecommerce_api.service.inf.IOrderService;
import com.harikrish.ecommerce_api.service.inf.IUserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements IOrderService {
    private  final OrderRepository orderRepository;
    private  final IUserService userService;
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;

    @Override
    @Transactional
    public OrderResponse createOrder(OrderShippingAddressRequest request) {
        User currentUser = userService.getCurrentUser();
        Cart cart = cartRepository.findById(currentUser.getId())
                .orElseThrow(()-> new ResourceNotFound("Cart not found for user "));

        if(cart.getCartItems().isEmpty()){
            throw new ResourceNotFound("Cart is empty. Cannot create order.");
        }
        Order order =  Order.builder()
                .user(currentUser)
                .shippingAddress(request.getShippingAddress())
                .status(OrderStatus.PENDING)
                .totalAmount(0.0)
                .build();
        double totalAmount = 0.0;
        for(CartItem cartItem : cart.getCartItems()){
            Product product = cartItem.getProduct();
            if(product.getQuantity() < cartItem.getQuantity()){
                throw new BadRequestException("Product "+ product.getTitle() + " is out of stock.");
            }
            OrderItem orderItem = OrderItem.builder()
                    .order(order)
                    .priceAtPurchase(product.getPrice())
                    .quantity(cartItem.getQuantity())
                    .product(product)
                    .build();
            order.getOrderItems().add(orderItem);
            product.setQuantity(product.getQuantity() - cartItem.getQuantity());
            productRepository.save(product);

            totalAmount += product.getPrice() * cartItem.getQuantity();
        }
        order.setTotalAmount(totalAmount);
        Order saved = orderRepository.save(order);
        cart.getCartItems().clear();
        cartRepository.save(cart);
        return mapToOrderResponse(saved);
    }

    @Override
    public List<OrderResponse> getMyOrders() {
        User currentUser = userService.getCurrentUser();

        return orderRepository.findByUserId(currentUser.getId()).stream()
                .map(this::mapToOrderResponse).toList();
    }
    public OrderResponse mapToOrderResponse(Order order){
        List<OrderItemResponse> orderItems = order.getOrderItems().stream()
                .map(this::mapToOrderItemResponse)
                .toList();
        return OrderResponse.builder()
                .id(order.getId())
                .orderDate(order.getOrderDate())
                .status(order.getStatus())
                .shippingAddress(order.getShippingAddress())
                .orderItems(orderItems)
                .totalAmount(order.getTotalAmount())
                .userId(order.getUser().getId())
                .build();
    }
    public OrderItemResponse mapToOrderItemResponse(OrderItem orderItem){
        return OrderItemResponse.builder()
                .id(orderItem.getId())
                .productId(orderItem.getProduct().getId())
                .quantity(orderItem.getQuantity())
                .productModel(orderItem.getProduct().getModel())
                .priceAtPurchase(orderItem.getPriceAtPurchase())
                .subTotal(orderItem.getPriceAtPurchase()  * orderItem.getQuantity())
                .productTitle(orderItem.getProduct().getTitle())
                .productBrand(orderItem.getProduct().getBrand())
                .productImageUrl(orderItem.getProduct().getImageUrl())
                .build();
    }
    @Override
    public OrderResponse getMyOrderById(Long id) {
        return mapToOrderResponse(orderRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFound("Order not found")));
    }

    @Override
    @Transactional
    public OrderResponse updateOrderStatus(UpdateOrderStatusRequest request) {
        Order  order = orderRepository.findById(request.getId())
                .orElseThrow(()-> new ResourceNotFound("Order not found"));

        if(order.getStatus().equals(OrderStatus.DELIVERED) || order.getStatus().equals(OrderStatus.CANCELLED)){
            throw new BadRequestException("Cannot update status of delivered or cancelled order");
        }
        order.setStatus(request.getStatus());
        if(request.getStatus().equals(OrderStatus.CANCELLED)){
            for(OrderItem item : order.getOrderItems()){
                Product product = item.getProduct();
                product.setQuantity(product.getQuantity() + item.getQuantity());
                productRepository.save(product);
            }
        }
        return mapToOrderResponse(orderRepository.save(order));
    }


}
