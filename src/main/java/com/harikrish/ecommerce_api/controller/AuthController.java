package com.harikrish.ecommerce_api.controller;

import com.harikrish.ecommerce_api.model.dto.request.LoginRequest;
import com.harikrish.ecommerce_api.model.dto.request.RegisterRequest;
import com.harikrish.ecommerce_api.model.dto.response.AuthResponse;
import com.harikrish.ecommerce_api.service.inf.IAuthService;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final IAuthService authService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody RegisterRequest request) {
        String response = authService.register(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);

        // Why Using the ResponseEntity?
        // ResponseEntity allows us to customize the HTTP response, including the status code, headers, and body.
        // In this case, we want to return a 201 Created status code to indicate that a new resource (user) has been successfully created.
        // By using ResponseEntity, we can easily set the status code and return the appropriate response body in a clean and readable way.
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> loginRequest(@Valid @RequestBody LoginRequest request) {
        AuthResponse authResponse = authService.login(request);
        return ResponseEntity.ok(authResponse); // This will return the token in the response body with a 200 OK status.

    }

    @GetMapping("csrf-token")
    public CsrfToken getCsrfToken(HttpServletRequest request) {
        return (CsrfToken) request.getAttribute("_csrf");
    }

}