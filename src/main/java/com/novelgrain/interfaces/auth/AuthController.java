// src/main/java/com/novelgrain/interfaces/auth/AuthController.java
package com.novelgrain.interfaces.auth;

import com.novelgrain.application.user.UserService;
import com.novelgrain.common.ApiResponse;

import com.novelgrain.auth.CodeStore;
import com.novelgrain.auth.InMemoryCodeStore;
import com.novelgrain.auth.SmsService;
import com.novelgrain.auth.impl.MockSmsService;
import com.novelgrain.infrastructure.jpa.entity.UserPO;
import com.novelgrain.security.JwtUtil;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Random;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    // 验证码仍用内存；以后可换 Redis 实现
    private final CodeStore codeStore = new InMemoryCodeStore();
    private final SmsService smsService = new MockSmsService();
    private final Random random = new Random();

    private final UserService userService;

    @PostMapping(value="/phone/code",
            consumes=MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<?> sendPhoneCode(@RequestBody PhoneCodeReq req) throws Exception {
        String phone = req.getPhone();
        if (phone == null || phone.isBlank()) return ApiResponse.error(400, "手机号不能为空");
        String code = String.format("%06d", random.nextInt(1000000));
        codeStore.save("login", phone, code, 300);
        smsService.sendLoginCode(phone, code);  // 控制台会打印验证码
        return ApiResponse.ok(Map.of("sent", true, "ttl", 300));
    }

    @PostMapping(value="/phone/login",
            consumes=MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<?> phoneLogin(@RequestBody PhoneLoginReq req) {
        String phone = req.getPhone();
        String code  = req.getCode();
        String cached = codeStore.get("login", phone);
        if (cached == null || !cached.equals(code)) return ApiResponse.error(401, "验证码错误或已过期");
        codeStore.remove("login", phone);

        // 查库/落库
        UserPO user = userService.upsertByPhone(phone);

        String token = JwtUtil.createToken(user.getId(), user.getNick(), 7L*24*3600*1000);
        return ApiResponse.ok(Map.of(
                "token", token,
                "user", Map.of(
                        "id", user.getId(),
                        "nick", user.getNick(),
                        "avatar", user.getAvatar(),
                        "phone", user.getPhone()
                )
        ));
    }

    // 占位：如果后续接入微信 openid，可在这里换 openid -> upsert
    @PostMapping(value="/wechat/login",
            consumes=MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<?> wechatLogin(@RequestBody Map<String,String> body) {
        String wechatCode = body.get("wechatCode");
        if (wechatCode == null || wechatCode.isBlank()) return ApiResponse.error(400, "wechatCode 不能为空");
        // TODO: 用 wechatCode 调用微信接口换 openid；这里暂用 code 的 hash 代替
        String openid = "mock_openid_" + Math.abs(wechatCode.hashCode());
        var u = userService.upsertByWechatOpenid(openid, "微信用户", null);
        String token = JwtUtil.createToken(u.getId(), u.getNick(), 7L*24*3600*1000);
        return ApiResponse.ok(Map.of(
                "token", token,
                "user", Map.of("id", u.getId(), "nick", u.getNick(), "avatar", u.getAvatar())
        ));
    }

    @Data public static class PhoneCodeReq { private String phone; }
    @Data public static class PhoneLoginReq { private String phone; private String code; }
}
