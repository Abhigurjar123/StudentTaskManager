package org.example.studenttaskmanager.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long jwtExpiration;

    // ================== GENERATE TOKEN ==================
    public String generateToken(UserDetails userDetails) {

        Date currentDate = new Date();
        Date expiryDate = new Date(currentDate.getTime() + jwtExpiration);

        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(currentDate)
                .setExpiration(expiryDate)
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // ================== EXTRACT ALL CLAIMS (COMMON METHOD) ==================
    private Claims extractAllClaims(String token) {

        return Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // ================== EXTRACT USERNAME ==================
    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    // ================== CHECK IF TOKEN EXPIRED ==================
    public boolean isTokenExpired(String token) {
        Date expiryDate = extractAllClaims(token).getExpiration();
        return expiryDate.before(new Date());
    }

    // ================== VALIDATE TOKEN (WITH EXCEPTION HANDLING) ==================
    public boolean isTokenValid(String token, UserDetails userDetails) {

        try {
            String username = extractUsername(token);

            return username.equals(userDetails.getUsername())
                    && !isTokenExpired(token);

        } catch (ExpiredJwtException e) {
            System.out.println("Token expired");
        } catch (JwtException e) {
            System.out.println("Invalid token");
        } catch (Exception e) {
            System.out.println("Something went wrong");
        }

        return false;
    }

    // ================== SECRET KEY ==================
    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}