package com.harikrish.ecommerce_api.service.impl;

import com.harikrish.ecommerce_api.exception.JwtValidationException;
import com.harikrish.ecommerce_api.service.inf.IJwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;


@Service
public class JwtServiceImpl implements IJwtService {

    private final String secretKey = "ThisIsASecretKeyForJwtGenerationAndItShouldBeLongEnoughToBeSecure";


    @Override
    public String generateToken(String email) {
        Map<String,Object> claims = new HashMap<>();
        claims.put("email",email);
//        claims.put("role","USER");
        return Jwts.builder()
                .claims(claims)
                .subject(email)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 5))// Token valid for 50 hours
                .signWith(getKey())
                .compact();

    }

    @Override
    public boolean validationToken(String token, UserDetails userDetails) {
        final String email = extractUserEmail(token);
        return (email.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private SecretKey getKey(){
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver){
        try {
            final Claims claims = extractAllClaims(token);
            return claimsResolver.apply(claims);
        }catch(Exception ex){
            throw new JwtValidationException("Jwt Token");
        }


    }
    public Claims extractAllClaims(String token){
        return Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload() ;
    }

    public String extractUserEmail(String token){
        return extractClaim(token, Claims::getSubject);
    }

    public boolean isTokenExpired(String token){
        final Date expiration = extractClaim(token, Claims::getExpiration);
        return expiration.before(new Date());
    }

//    private String getSecretKey(){
//        try{
//            KeyGenerator keyGenerator = KeyGenerator.getInstance("HmacSHA256");
//            SecretKey secretKey = keyGenerator.generateKey();
//            return Base64.getEncoder().encodeToString(secretKey.getEncoded());
//        }
//        catch (NoSuchAlgorithmException ex){
//            throw  new RuntimeException("Error generating secret key for JWT", ex);
//        }
//    }

}
