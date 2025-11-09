//package com.mycompany.property_management.config;
//
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.SignatureAlgorithm;
//import io.jsonwebtoken.security.Keys;
//import io.jsonwebtoken.io.Decoders;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//
//import java.security.Key;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.function.Function;
//
//@Component
//public class JwtTokenUtil {
//
//    @Value("${jwt.validity}")
//    private long jwtTokenValidity;
//
//    @Value("${jwt.secret}")
//    private String secret;
//
//    /**
//     * Helper method to get the signing key from the Base64 secret
//     */
//    private Key getSigningKey() {
//        byte[] keyBytes = Decoders.BASE64.decode(this.secret);
//        return Keys.hmacShaKeyFor(keyBytes);
//    }
//
//    // --- Public Methods ---
//
//    /**
//     * Generates a new JWT token for a given username.
//     */
//    public String generateToken(String username) {
//        Map<String, Object> claims = new HashMap<>();
//        return doGenerateToken(claims, username);
//    }
//
//    /**
//     * Validates a token by checking its signature and expiration.
//     */
//    public Boolean validateToken(String token) {
//        try {
//            // Jwts.parserBuilder() will fail if the signature is invalid
//            getAllClaimsFromToken(token);
//            return !isTokenExpired(token);
//        } catch (Exception e) {
//            // Catches all parse errors (Malformed, Expired, Signature, etc)
//            System.out.println("Invalid JWT token: {}"+ e.getMessage());
//            return false;
//        }
//    }
//
//    /**
//     * Retrieves the username (subject) from the token.
//     */
//    public String getUsernameFromToken(String token) {
//        return getClaimFromToken(token, Claims::getSubject);
//    }
//
//
//    // --- Private Helper Methods ---
//
//    /**
//     * Checks if the token has expired.
//     */
//    private Boolean isTokenExpired(String token) {
//        final Date expiration = getClaimFromToken(token, Claims::getExpiration);
//        return expiration.before(new Date());
//    }
//
//    /**
//     * Retrieves a specific claim from the token.
//     */
//    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
//        final Claims claims = getAllClaimsFromToken(token);
//        return claimsResolver.apply(claims);
//    }
//
//    /**
//     * This is the core parser method. It decodes the token, validates the signature,
//     * and returns all claims. It's designed to throw an error if validation fails.
//     */
//    private Claims getAllClaimsFromToken(String token) {
//        return Jwts.parserBuilder()
//                .setSigningKey(getSigningKey())
//                .build()
//                .parseClaimsJws(token)
//                .getBody();
//    }
//
//    /**
//     * The main token creation method.
//     */
//    private String doGenerateToken(Map<String, Object> claims, String subject) {
//        return Jwts.builder()
//                .setClaims(claims)
//                .setSubject(subject) // The "subject" is the username
//                .setIssuedAt(new Date(System.currentTimeMillis()))
//                .setExpiration(new Date(System.currentTimeMillis() + jwtTokenValidity))
//                .signWith(getSigningKey(), SignatureAlgorithm.HS512) // Use a strong algorithm
//                .compact();
//    }
//}