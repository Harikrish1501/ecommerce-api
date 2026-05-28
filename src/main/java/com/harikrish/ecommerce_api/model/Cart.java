package com.harikrish.ecommerce_api.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "carts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id" ,nullable = false)
    private User user;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<CartItem> cartItems = new ArrayList<>();

    public void addItem(CartItem cartItem){ // This is Helper Method
        cartItems.add(cartItem);
        cartItem.setCart(this);
    }
    public void removeItem(CartItem cartItem){ // This is Helper Method
        cartItems.remove(cartItem);
        cartItem.setCart(null);
    }


}
