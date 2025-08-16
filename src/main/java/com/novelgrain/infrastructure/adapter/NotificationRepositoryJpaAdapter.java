package com.novelgrain.infrastructure.adapter;

import com.novelgrain.domain.notification.NotificationItem;
import com.novelgrain.domain.notification.NotificationRepository;
import com.novelgrain.infrastructure.jpa.entity.NotificationPO;
import com.novelgrain.infrastructure.jpa.repo.NotificationJpa;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationRepositoryJpaAdapter implements NotificationRepository {
    private final NotificationJpa notificationJpa;

    @Override
    public Page<NotificationItem> page(Long userId, int page, int size) {
        var p = notificationJpa.findByUserIdOrderByCreatedAtDesc(userId, PageRequest.of(page - 1, size));
        return p.map(this::map);
    }

    @Override
    public long markAllRead(Long userId) {
        var p = notificationJpa.findByUserIdOrderByCreatedAtDesc(userId, PageRequest.of(0, 1000));
        p.forEach(n -> {
            n.setRead(true);
        });
        notificationJpa.saveAll(p);
        return 0L;
    }

    @Override
    public void markRead(Long userId, Long id) {
        var n = notificationJpa.findById(id).orElseThrow();
        n.setRead(true);
        notificationJpa.save(n);
    }

    @Override
    public long unreadCount(Long userId) {
        return notificationJpa.countByUserIdAndReadFalse(userId);
    }

    private NotificationItem map(NotificationPO n) {
        return NotificationItem.builder()
                .id(n.getId()).type(n.getType()).title(n.getTitle()).content(n.getContent())
                .bookId(n.getBook() != null ? n.getBook().getId() : null)
                .fromUserId(n.getActor() != null ? n.getActor().getId() : null)
                .fromUserName(n.getActor() != null ? n.getActor().getNick() : null)
                .fromUserAvatar(n.getActor() != null ? n.getActor().getAvatar() : null)
                .read(n.getRead() != null && n.getRead())
                .createdAt(n.getCreatedAt().atZone(java.time.ZoneId.systemDefault()).toInstant())
                .build();
    }
}
