package com.example.studentmanagement.security;


import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtil {

    private static final String SECRETE_KEY = "mySecretKeyForJwtToken5885949043GenerationMustBeLongEnough";
    private final long ACCESS_TOKEN_EXPIRATION = 1000 * 60 * 60; // 1 hour

    private final long REFRESH_TOKEN_EXPIRATION =
            1000L * 60 * 60 * 24 * 7; // 7 days


    private SecretKey getSigningkey() {

        return Keys.hmacShaKeyFor(SECRETE_KEY.getBytes(StandardCharsets.UTF_8));

    }

//    public String generateToken(String email) {
//
//        return Jwts.builder().
//                subject(email).
//                issuedAt(new Date()).
//                expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME)).
//                signWith(getSigningkey()).
//                compact();
//
//    }
    public String generateRefreshToken(String email) {

        return Jwts.builder()
                .subject(email)
                .issuedAt(new Date())
                .expiration(new Date(
                        System.currentTimeMillis() + REFRESH_TOKEN_EXPIRATION
                ))
                .signWith(getSigningkey())
                .compact();
    }

    public String generateAccessToken(String email) {

        return Jwts.builder()
                .subject(email)
                .issuedAt(new Date())
                .expiration(new Date(
                        System.currentTimeMillis() + ACCESS_TOKEN_EXPIRATION
                ))
                .signWith(getSigningkey())
                .compact();
    }

    public String extractEmail(String token){

        return Jwts.parser().
                verifyWith(getSigningkey()).
                build().
                parseSignedClaims(token).
                getPayload().
                getSubject();

    }

    public boolean validateToken(String token,String email){

        String extractedEmail=extractEmail(token);

        return extractedEmail.equals(email) && !isTokenExpired(token);

    }

    private boolean isTokenExpired(String token){

        Date expirationDate=Jwts.parser()
                .verifyWith(getSigningkey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getExpiration();


        return expirationDate.before(new Date());

    }

}
