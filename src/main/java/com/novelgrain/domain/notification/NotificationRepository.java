package com.novelgrain.domain.notification;

import org.springframework.data.domain.Page;

public interface NotificationRepository {
    Page<NotificationItem> page(Long userId, String type, int page, int size);

    long markAllRead(Long userId);

    void markRead(Long userId, Long id);

    long unreadCount(Long userId);
}
