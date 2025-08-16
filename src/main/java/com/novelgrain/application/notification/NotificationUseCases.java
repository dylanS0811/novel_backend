package com.novelgrain.application.notification;

import com.novelgrain.common.PageResponse;
import com.novelgrain.domain.notification.NotificationItem;
import com.novelgrain.domain.notification.NotificationRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationUseCases {
    private final NotificationRepository repo;

    public PageResponse<NotificationItem> page(Long userId, int page, int size) {
        Page<NotificationItem> p = repo.page(userId, page, size);
        return new PageResponse<>(p.getContent(), page, size, p.getTotalElements());
    }

    public long markAllRead(Long userId) {
        return repo.markAllRead(userId);
    }

    public void markRead(Long userId, Long id) {
        repo.markRead(userId, id);
    }

    public long unread(Long userId) {
        return repo.unreadCount(userId);
    }
}
