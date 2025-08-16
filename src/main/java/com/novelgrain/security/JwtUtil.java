package com.novelgrain.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;
import java.util.Map;

public class JwtUtil {
    // 随机生成 256-bit 秘钥（正式环境放配置里）
    private static final Key KEY = Keys.hmacShaKeyFor(
            "novelgrain-very-secret-key-please-change-32bytes".getBytes());

    public static String createToken(Long userId, String nick, long ttlMillis) {
        long now = System.currentTimeMillis();
        return Jwts.builder()
                .setSubject(String.valueOf(userId))
                .addClaims(Map.of("nick", nick))
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + ttlMillis))
                .signWith(KEY, SignatureAlgorithm.HS256)
                .compact();
    }

    public static Jws<Claims> parse(String token) throws JwtException {
        return Jwts.parserBuilder().setSigningKey(KEY).build().parseClaimsJws(token);
    }
}
