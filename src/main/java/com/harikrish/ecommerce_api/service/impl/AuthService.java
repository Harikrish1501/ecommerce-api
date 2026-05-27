package com.harikrish.ecommerce_api.service.impl;

import com.harikrish.ecommerce_api.exception.BadRequestException;
import com.harikrish.ecommerce_api.model.Cart;
import com.harikrish.ecommerce_api.model.User;
import com.harikrish.ecommerce_api.model.WishList;
import com.harikrish.ecommerce_api.model.dto.request.LoginRequest;
import com.harikrish.ecommerce_api.model.dto.request.RegisterRequest;
import com.harikrish.ecommerce_api.model.dto.response.AuthResponse;
import com.harikrish.ecommerce_api.model.enums.UserRole;
import com.harikrish.ecommerce_api.repository.CartRepository;
import com.harikrish.ecommerce_api.repository.UserRepo;
import com.harikrish.ecommerce_api.repository.WishlistRepository;
import com.harikrish.ecommerce_api.service.inf.IAuthService;
import com.harikrish.ecommerce_api.service.inf.IJwtService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService implements IAuthService {
    private final UserRepo userRepo;
    private final CartRepository  cartRepository;
    private final WishlistRepository wishlistRepository;
    private  final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
    private final AuthenticationManager authenticationManager;
    private final IJwtService jwtService;
    @Override
    @Transactional
    public String register(RegisterRequest request) {
        if(userRepo.existsByEmail(request.getEmail())){
          throw  new BadRequestException("Email already exists");
        }
        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .build();
        User savedUser = userRepo.save(user);

        if(request.getRole().equals(UserRole.USER)){
            Cart cart = Cart.builder()
                    .user(savedUser)
                    .build();
            cartRepository.save(cart);

            WishList wishList  = WishList.builder()
                    .user(savedUser)
                    .build();
            wishlistRepository.save(wishList);
        }
        return "User Create Successfully";
    }

    @Override
    @Transactional
    public AuthResponse login(LoginRequest request) {
//        User user = userRepo.findByEmail(request.getEmail())
//                .orElseThrow(()->new BadRequestException("Invalid email or password"));
//        if(!user.getPassword().equals(passwordEncoder.encode(request.getPassword()))){
//                throw new BadRequestException("Invalid email or password");
//        }
//
//        return AuthResponse.builder()
//                .token("dummy-token")
//                .build();

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        if(authentication.isAuthenticated()){
            return AuthResponse.builder()
                    .token(jwtService.generateToken(request.getEmail())).build();
        }
        else{
            return AuthResponse.builder().token("Failed").build();
        }
    }
}
