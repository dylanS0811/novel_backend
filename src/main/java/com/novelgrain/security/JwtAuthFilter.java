package com.novelgrain.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import io.jsonwebtoken.Claims;

import java.io.IOException;
import java.util.List;

public class JwtAuthFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res,
                                    FilterChain chain) throws ServletException, IOException {
        String auth = req.getHeader(HttpHeaders.AUTHORIZATION);
        if (auth != null && auth.startsWith("Bearer ")) {
            String token = auth.substring(7);
            try {
                Claims c = JwtUtil.parse(token).getBody();
                Long uid = Long.valueOf(c.getSubject());
                String nick = (String) c.get("nick");
                var authToken = new AbstractAuthenticationToken(List.of()) {
                    @Override public Object getCredentials() { return token; }
                    @Override public Object getPrincipal() { return uid; }
                };
                authToken.setDetails(nick);
                authToken.setAuthenticated(true);
                SecurityContextHolder.getContext().setAuthentication(authToken);
            } catch (Exception ignore) {
                res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
        }
        chain.doFilter(req, res);
    }
}
