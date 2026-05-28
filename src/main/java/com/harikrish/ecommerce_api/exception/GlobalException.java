package com.harikrish.ecommerce_api.exception;

import com.harikrish.ecommerce_api.model.dto.response.ErrorResponse;
import org.springframework.boot.context.config.ConfigDataResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice // This is like to Catch block ,All Exceptions will be handled here
public class GlobalException {

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> handelBadRequest(BadRequestException ex){
        ErrorResponse errorResponse  = ErrorResponse.builder()
                .timestamp(LocalDateTime.now().toString())
                .status(HttpStatus.BAD_REQUEST.value())
                .error("Bad Request")
                .message(ex.getMessage())
                .build();
        return new ResponseEntity<>(errorResponse , HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceNotFound.class)
    public ResponseEntity<ErrorResponse> handelResourceNotFoundRequest(ResourceNotFound ex){
        ErrorResponse errorResponse  = ErrorResponse.builder()
                .timestamp(LocalDateTime.now().toString())
                .status(HttpStatus.NOT_FOUND.value())
                .error("Resource Not Found")
                .message(ex.getMessage())
                .build();
        return new ResponseEntity<>(errorResponse , HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handelValidException(MethodArgumentNotValidException ex){
        Map<String , String > errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error->{
            String fieldName = ((FieldError)error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName , errorMessage);
        });
        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now().toString())
                .status(HttpStatus.BAD_REQUEST.value())
                .error("Validation Error")
                .message(errors.toString())
                .build();
        return new ResponseEntity<>(errorResponse , HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handelException(Exception ex){
        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now().toString())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .error("Internal Server Error")
                .message("An unexcepted situation occurred. Please try again later.")
                .build();
        return new ResponseEntity<>(errorResponse , HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorResponse> handleUnauthorizedRequest(UnauthorizedException ex){
        ErrorResponse errorResponse  = ErrorResponse.builder()
                .timestamp(LocalDateTime.now().toString())
                .status(HttpStatus.UNAUTHORIZED.value())
                .error("Unauthorized Request")
                .message(ex.getMessage())
                .build();
        return new ResponseEntity<>(errorResponse , HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentials(BadCredentialsException ex){
        ErrorResponse errorResponse  = ErrorResponse.builder()
                .timestamp(LocalDateTime.now().toString())
                .status(HttpStatus.UNAUTHORIZED.value())
                .error("Unauthorized Request")
                .message("Invalid email or password. Please try again.")
                .build();
        return new ResponseEntity<>(errorResponse , HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAuthorizationDenied(AuthorizationDeniedException ex) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now().toString())
                .status(HttpStatus.UNAUTHORIZED.value())
                .error("Unauthorized Request")
                .message("You do not have permission to access this resource.")
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);

    }

}
