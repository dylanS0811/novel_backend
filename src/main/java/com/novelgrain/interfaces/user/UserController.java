package com.novelgrain.interfaces.user;

import com.novelgrain.application.user.UserService;
import com.novelgrain.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/check-nickname")
    public ApiResponse<Object> checkNickname(@RequestParam("nickname") String nickname) {
        boolean exists = userService.nickExists(nickname);
        return ApiResponse.ok(java.util.Map.of("exists", exists));
    }
}
