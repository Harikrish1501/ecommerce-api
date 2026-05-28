package com.harikrish.ecommerce_api.service.inf;

import com.harikrish.ecommerce_api.model.dto.request.LoginRequest;
import com.harikrish.ecommerce_api.model.dto.request.RegisterRequest;
import com.harikrish.ecommerce_api.model.dto.response.AuthResponse;

public interface IAuthService {

    String register(RegisterRequest request);

    AuthResponse login(LoginRequest request);
}
