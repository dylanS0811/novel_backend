package com.novelgrain.interfaces.auth;

import com.novelgrain.application.user.UserService;
import com.novelgrain.common.ApiResponse;
import com.novelgrain.infrastructure.jpa.entity.UserPO;
import com.novelgrain.security.JwtUtil;
import jakarta.validation.Valid;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;
    private final PasswordEncoder encoder = new BCryptPasswordEncoder();

    private static final Pattern EMAIL = Pattern.compile("^[\\w.+-]+@[\\w.-]+\\.[A-Za-z]{2,}$");
    private static final Pattern PHONE = Pattern.compile("^\\d{7,15}$");
    private static final Pattern USERNAME = Pattern.compile("^(?=.{3,20}$)(?!.*[._-]{2})[A-Za-z0-9]+(?:[._-][A-Za-z0-9]+)*$");

    @PostMapping(value="/register", consumes=MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<?> register(@RequestBody @Valid CredentialReq req) {
        String handle = req.getHandle();
        String nick = req.getNick();
        String password = req.getPassword();
        if (!validHandle(handle)) return ApiResponse.error(400, "用户名 / 邮箱 / 手机号 格式不正确");
        if (password == null || password.length() < 6) return ApiResponse.error(400, "密码至少6位");
        if (nick == null || nick.isBlank()) return ApiResponse.error(400, "昵称不能为空");
        if (userService.handleExists(handle)) return ApiResponse.error(409, "用户名 / 邮箱 / 手机号 已存在");
        if (userService.nickExists(nick)) return ApiResponse.error(409, "昵称已存在");
        String hash = encoder.encode(password);
        userService.createUser(handle, nick, hash);
        return ApiResponse.ok(Map.of("ok", true));
    }

    @PostMapping(value="/login", consumes=MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<?> login(@RequestBody @Valid CredentialReq req) {
        String handle = req.getHandle();
        String password = req.getPassword();
        if (!validHandle(handle)) return ApiResponse.error(400, "用户名 / 邮箱 / 手机号 格式不正确");
        if (password == null || password.length() < 6) return ApiResponse.error(400, "密码至少6位");
        UserPO user = userService.findByHandle(handle).orElse(null);
        if (user == null || user.getPasswordHash() == null || !encoder.matches(password, user.getPasswordHash())) {
            return ApiResponse.error(401, "用户名或密码错误");
        }
        String token = JwtUtil.createToken(user.getId(), user.getNick(), 7L*24*3600*1000);
        Map<String, Object> userMap = new java.util.HashMap<>();
        userMap.put("id", user.getId());
        userMap.put("nick", user.getNick());
        userMap.put("avatar", user.getAvatar());
        userMap.put("username", user.getUsername());
        userMap.put("email", user.getEmail());
        userMap.put("phone", user.getPhone());
        return ApiResponse.ok(Map.of(
                "token", token,
                "user", userMap
        ));
    }

    private boolean validHandle(String handle) {
        if (handle == null) return false;
        return EMAIL.matcher(handle).matches() || PHONE.matcher(handle).matches() || USERNAME.matcher(handle).matches();
    }

    @Data
    public static class CredentialReq {
        private String handle;
        private String nick;
        private String password;
    }
}
