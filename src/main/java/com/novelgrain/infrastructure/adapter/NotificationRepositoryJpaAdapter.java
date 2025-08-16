package com.novelgrain.infrastructure.adapter;

import com.novelgrain.domain.notification.NotificationItem;
import com.novelgrain.domain.notification.NotificationRepository;
import com.novelgrain.infrastructure.jpa.entity.BookPO;
import com.novelgrain.infrastructure.jpa.entity.CommentPO;
import com.novelgrain.infrastructure.jpa.entity.NotificationPO;
import com.novelgrain.infrastructure.jpa.entity.UserPO;
import com.novelgrain.infrastructure.jpa.repo.BookJpa;
import com.novelgrain.infrastructure.jpa.repo.CommentJpa;
import com.novelgrain.infrastructure.jpa.repo.NotificationJpa;
import com.novelgrain.infrastructure.jpa.repo.UserJpa;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.util.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class NotificationRepositoryJpaAdapter implements NotificationRepository {
    private final NotificationJpa notificationJpa;
    private final UserJpa userJpa;
    private final BookJpa bookJpa;
    private final CommentJpa commentJpa;

    @Override
    @Transactional(readOnly = true)
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

    @Override
    public boolean exists(String type, Long receiverId, Long actorId, Long bookId, Long commentId) {
        if (commentId == null) {
            return notificationJpa.existsByTypeAndUser_IdAndActor_IdAndBook_IdAndCommentIsNull(type, receiverId, actorId, bookId);
        }
        return notificationJpa.existsByTypeAndUser_IdAndActor_IdAndBook_IdAndComment_Id(type, receiverId, actorId, bookId, commentId);
    }

    @Override
    public void save(String type, Long receiverId, Long actorId, Long bookId, Long commentId, String title, String content) {
        UserPO receiver = userJpa.findById(receiverId).orElseThrow();
        UserPO actor = userJpa.findById(actorId).orElseThrow();
        BookPO book = bookId != null ? bookJpa.findById(bookId).orElse(null) : null;
        CommentPO comment = commentId != null ? commentJpa.findById(commentId).orElse(null) : null;

        NotificationPO po = NotificationPO.builder()
                .type(type)
                .title(title)
                .content(content)
                .user(receiver)
                .actor(actor)
                .book(book)
                .comment(comment)
                .read(false)
                .createdAt(java.time.LocalDateTime.now())
                .build();
        notificationJpa.save(po);
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
