// src/main/java/com/novelgrain/interfaces/user/MeController.java
package com.novelgrain.interfaces.user;


import com.novelgrain.application.user.UserService;
import com.novelgrain.common.ApiResponse;
import com.novelgrain.infrastructure.jpa.entity.UserPO;
import com.novelgrain.security.JwtUtil;

import io.jsonwebtoken.Claims;

import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;

import lombok.RequiredArgsConstructor;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MeController {

    private final UserService userService;

    @GetMapping("/me")
    public ApiResponse<?> me(HttpServletRequest req) {
        Long uid = currentUserId(req);
        if (uid == null) return ApiResponse.err(401, "未登录");
        UserPO u = userService.getById(uid).orElse(null);
        if (u == null) return ApiResponse.err(404, "用户不存在");
        return ApiResponse.ok(Map.of(
                "id", u.getId(),
                "nick", u.getNick(),
                "avatar", u.getAvatar(),
                "username", u.getUsername(),
                "email", u.getEmail(),
                "phone", u.getPhone()
        ));
    }

    @PatchMapping("/me")
    public ApiResponse<?> patchMe(@RequestBody Map<String, Object> body, HttpServletRequest req) {
        Long uid = currentUserId(req);
        if (uid == null) return ApiResponse.err(401, "未登录");
        String nick = (String) (body.getOrDefault("nickname", body.get("nick")));
        String avatar = (String) body.get("avatar");
        if (nick != null && userService.nickExistsForOther(nick, uid)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "昵称已被占用");
        }
        UserPO u = userService.updateProfile(uid, nick, avatar);
        return ApiResponse.ok(Map.of(
                "id", u.getId(),
                "nick", u.getNick(),
                "avatar", u.getAvatar(),
                "username", u.getUsername(),
                "email", u.getEmail(),
                "phone", u.getPhone()
        ));
    }

    private Long currentUserId(HttpServletRequest req) {
        try {
            var auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && auth.isAuthenticated() && auth.getPrincipal() instanceof Long l) return l;
        } catch (Exception ignore) {
        }
        String h = req.getHeader("Authorization");
        if (h != null && h.startsWith("Bearer ")) {
            try {
                Claims c = JwtUtil.parse(h.substring(7)).getBody();
                return Long.valueOf(c.getSubject());
            } catch (Exception ignore) {
            }
        }
        return null;
    }
}
