package com.harikrish.ecommerce_api.exception;

public class JwtValidationException extends RuntimeException {
    public JwtValidationException(String message) {
        super(message);
    }
}
