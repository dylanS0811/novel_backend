package com.novelgrain.interfaces.notification;

import com.novelgrain.application.notification.NotificationUseCases;
import com.novelgrain.common.ApiResponse;
import com.novelgrain.common.PageResponse;
import com.novelgrain.domain.notification.NotificationItem;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationUseCases use;

    @GetMapping
    public ApiResponse<PageResponse<NotificationItem>> list(@RequestParam Long userId,
                                                            @RequestParam(required = false) String type,
                                                            @RequestParam(defaultValue = "1") int page,
                                                            @RequestParam(defaultValue = "20") int size) {
        return ApiResponse.ok(use.page(userId, type, page, size));
    }

    @PostMapping("/read-all")
    public ApiResponse<Object> readAll(@RequestParam Long userId) {
        use.markAllRead(userId);
        return ApiResponse.ok(java.util.Map.of("unreadCount", 0));
    }

    @PostMapping("/{id}/read")
    public ApiResponse<Object> readOne(@PathVariable Long id, @RequestParam Long userId) {
        use.markRead(userId, id);
        return ApiResponse.ok(null);
    }

    @GetMapping("/unread-count")
    public ApiResponse<Object> unreadCount(@RequestParam Long userId) {
        long c = use.unread(userId);
        return ApiResponse.ok(java.util.Map.of("unreadCount", c));
    }
}
