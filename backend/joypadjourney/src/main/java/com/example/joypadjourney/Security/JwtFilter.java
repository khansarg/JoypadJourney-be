package com.example.joypadjourney.Security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
        ) throws ServletException, IOException {
    
        String path = request.getRequestURI();
        System.out.println("Incoming request path: " + path);
    
        // Lewati filter untuk endpoint tertentu
        if (path.equals("/api/customer/register") || path.equals("/api/auth/login")) {
            filterChain.doFilter(request, response);
            return;
        }
    
        String token = request.getHeader("Authorization");
        System.out.println("Authorization Header: " + token);
    
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7); // Hapus "Bearer " dari token
            try {
                Claims claims = jwtUtil.validateToken(token);
                System.out.println("Token is valid. Claims: " + claims);
                request.setAttribute("claims", claims); // Simpan claims jika diperlukan
            } catch (Exception e) {
                System.err.println("Token validation failed: " + e.getMessage());
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
        } else {
            System.err.println("Missing or invalid Authorization header.");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
    
        filterChain.doFilter(request, response);
    }
    
}
