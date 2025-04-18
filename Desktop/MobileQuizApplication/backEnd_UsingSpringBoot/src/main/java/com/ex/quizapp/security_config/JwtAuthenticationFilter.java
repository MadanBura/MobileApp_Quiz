package com.ex.quizapp.security_config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.OncePerRequestFilter;
import com.ex.quizapp.services.JwtService;
import com.ex.quizapp.services.TokenBlacklistService;

import java.io.IOException;
import java.util.Arrays;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    
   
//    @Autowired
//    private TokenBlacklistService tokenBlacklistService;


    public JwtAuthenticationFilter(JwtService jwtService, UserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");
        System.out.println("Authorization Header: " + authHeader); // Debugging Log

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            System.out.println("No valid token found in the request.");
//            if (tokenBlacklistService.isTokenBlacklisted(token)) {
//                throw new RuntimeException("Token is invalid (User logged out)");
//            }
            chain.doFilter(request, response);
            return;
        }
        String token = "";
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
        	 token = authHeader.substring(7); // Remove "Bearer " correctly
        }
//         String jwtToken = authHeader.substring(7);
        System.out.println("Extracted Token: " + token); // Debugging Log

        String username = null;
        try {
            username = jwtService.extractUsername(token);
        } catch (Exception e) {
            System.out.println("Error extracting username from token: " + e.getMessage());
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            if (jwtService.validateToken(token, userDetails)) {
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authToken);
                System.out.println("User authenticated successfully: " + username);
            } else {
                System.out.println("Invalid token for user: " + username);
            }
        } else {
            System.out.println("Username is null or user already authenticated.");
        }

        chain.doFilter(request, response);
    }
    
}
