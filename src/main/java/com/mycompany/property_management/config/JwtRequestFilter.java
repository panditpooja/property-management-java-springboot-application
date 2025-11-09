//package com.mycompany.property_management.config; // Or a new '.filter' package
//
//import com.mycompany.property_management.config.JwtTokenUtil;
//import io.jsonwebtoken.ExpiredJwtException;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import java.io.IOException;
//import java.util.ArrayList;
//
//@Component
//public class JwtRequestFilter extends OncePerRequestFilter {
//
//    private static final Logger logger = LoggerFactory.getLogger(JwtRequestFilter.class);
//
//    @Autowired
//    private JwtTokenUtil jwtTokenUtil;
//
//    /**
//     * This method is the "bouncer" that checks every request.
//     */
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
//            throws ServletException, IOException {
//
//        final String requestTokenHeader = request.getHeader("Authorization");
//
//        String username = null;
//        String jwtToken = null;
//
//        // 1. Check if the Authorization header exists and starts with "Bearer "
//        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
//            jwtToken = requestTokenHeader.substring(7); // Extract the token
//            try {
//                // Get the username (email) from the token
//                username = jwtTokenUtil.getUsernameFromToken(jwtToken);
//            } catch (IllegalArgumentException e) {
//                logger.error("Unable to get JWT Token");
//            } catch (ExpiredJwtException e) {
//                logger.warn("JWT Token has expired");
//            } catch (Exception e) {
//                logger.error("JWT validation error: {}", e.getMessage());
//            }
//        } else if (requestTokenHeader != null) {
//            logger.warn("JWT Token does not begin with Bearer String");
//        }
//
//        // 2. If we got a token, validate it
//        // SecurityContextHolder.getContext().getAuthentication() == null ensures we only authenticate once
//        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
//
//            // Use the util to validate the token's signature and expiration
//            if (jwtTokenUtil.validateToken(jwtToken)) {
//
//                // --- This is the core of Spring Security authentication ---
//                // We create a "UserDetails" object (here, a simple one).
//                // In a real app, you'd fetch this from your database to get roles.
//                UserDetails userDetails = new User(username, "", new ArrayList<>());
//
//                // Create the standard Spring Security authentication token
//                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
//                        userDetails, null, userDetails.getAuthorities());
//
//                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//
//                // **This is the most important line**
//                // It "logs in" the user for this request by setting the
//                // authentication in Spring's Security Context.
//                SecurityContextHolder.getContext().setAuthentication(authToken);
//            }
//        }
//
//        // 3. Continue the filter chain
//        // This passes the request to the next filter, or to the controller.
//        chain.doFilter(request, response);
//    }
//}