package com.harikrish.ecommerce_api.configuration;

import com.harikrish.ecommerce_api.exception.JwtValidationException;
import com.harikrish.ecommerce_api.model.dto.response.ErrorResponse;
import com.harikrish.ecommerce_api.service.inf.IJwtService;
import com.harikrish.ecommerce_api.service.inf.IUserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.time.LocalDateTime;


@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final IJwtService jwtService;
    private final ApplicationContext context;



    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // Here you can implement your JWT validation logic
        // For example, you can extract the token from the Authorization header and validate it

        String token = null;
        String email = null;
        try {
            final String authHeader = request.getHeader("Authorization");
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                token = authHeader.substring(7);
                // Validate the token and set the authentication in the security context if valid
                // You can use your JwtService to validate the token and extract user details

                email = jwtService.extractUserEmail(token);
            } else {
                handleJwtValidationException(response, new JwtValidationException("Authorization header is missing or does not start with Bearer"));
                return;
            }
            if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = context.getBean(IUserService.class).loadUserByUsername(email);
                if (jwtService.validationToken(token, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }


            }
            filterChain.doFilter(request, response);
        } catch (Exception ex) {
            handleJwtValidationException(response, new JwtValidationException("Invalid JWT Token: " + ex.getMessage()));
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        // Exclude the registration and login endpoints from JWT validation
        return path.equals("/api/auth/register") || path.equals("/api/auth/login")
                || path.startsWith("/swagger-ui") || path.startsWith("/v3/api-docs");
    }

    private void handleJwtValidationException(HttpServletResponse response, JwtValidationException ex) throws IOException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json");
        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now().toString())
                .status(HttpStatus.UNAUTHORIZED.value())
                .error("Jwt Validation Failed")
                .message(ex.getMessage())
                .build();
        ObjectMapper mapper = new ObjectMapper();
        response.getWriter().write(mapper.writeValueAsString(errorResponse));

    }
}
