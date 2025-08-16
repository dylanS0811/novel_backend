package com.novelgrain.infrastructure.adapter;

import com.novelgrain.domain.notification.NotificationItem;
import com.novelgrain.domain.notification.NotificationRepository;
import com.novelgrain.infrastructure.jpa.entity.NotificationPO;
import com.novelgrain.infrastructure.jpa.repo.NotificationJpa;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.util.StringUtils;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationRepositoryJpaAdapter implements NotificationRepository {
    private final NotificationJpa notificationJpa;

    @Override
    public Page<NotificationItem> page(Long userId, String type, int page, int size) {
        var pageable = PageRequest.of(page - 1, size);
        Page<NotificationPO> p;
        if (StringUtils.hasText(type)) {
            p = notificationJpa.findByUserIdAndTypeOrderByCreatedAtDesc(userId, type, pageable);
        } else {
            p = notificationJpa.findByUserIdOrderByCreatedAtDesc(userId, pageable);
        }
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
        if (n.getUser() != null && n.getUser().getId().equals(userId)) {
            n.setRead(true);
            notificationJpa.save(n);
        }
    }

    @Override
    public long unreadCount(Long userId) {
        return notificationJpa.countByUserIdAndReadFalse(userId);
    }

    private NotificationItem map(NotificationPO n) {
        return NotificationItem.builder()
                .id(n.getId()).type(n.getType()).title(n.getTitle()).content(n.getContent())
                .bookId(n.getBook() != null ? n.getBook().getId() : null)
                .commentId(n.getComment() != null ? n.getComment().getId() : null)
                .actorId(n.getActor() != null ? n.getActor().getId() : null)
                .actorName(n.getActor() != null ? n.getActor().getNick() : null)
                .actorAvatar(n.getActor() != null ? n.getActor().getAvatar() : null)
                .read(n.getRead() != null && n.getRead())
                .createdAt(n.getCreatedAt().atZone(java.time.ZoneId.systemDefault()).toInstant())
                .build();
    }
}
