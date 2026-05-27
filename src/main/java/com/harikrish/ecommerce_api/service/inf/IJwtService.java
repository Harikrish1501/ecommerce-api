package com.harikrish.ecommerce_api.service.inf;

import org.springframework.security.core.userdetails.UserDetails;

import java.security.Key;

public interface IJwtService {
    public String generateToken(String email);
    public boolean validationToken(String token, UserDetails userDetails);
    public String extractUserEmail(String token);

}
