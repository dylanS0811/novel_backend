package com.novelgrain.application.notification;

import com.novelgrain.domain.notification.NotificationRepository;
import com.novelgrain.infrastructure.jpa.entity.BookPO;
import com.novelgrain.infrastructure.jpa.entity.CommentPO;
import com.novelgrain.infrastructure.jpa.repo.BookJpa;
import com.novelgrain.infrastructure.jpa.repo.CommentJpa;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository repo;
    private final BookJpa bookJpa;
    private final CommentJpa commentJpa;

    public void onBookLiked(Long bookId, Long actorId) {
        BookPO book = bookJpa.findById(bookId).orElseThrow();
        Long receiverId = book.getRecommender().getId();
        if (receiverId.equals(actorId)) return;
        if (repo.exists("BOOK_LIKED", receiverId, actorId, bookId, null)) return;
        repo.save("BOOK_LIKED", receiverId, actorId, bookId, null, book.getTitle(), null);
    }

    public void onBookBookmarked(Long bookId, Long actorId) {
        BookPO book = bookJpa.findById(bookId).orElseThrow();
        Long receiverId = book.getRecommender().getId();
        if (receiverId.equals(actorId)) return;
        if (repo.exists("BOOK_BOOKMARKED", receiverId, actorId, bookId, null)) return;
        repo.save("BOOK_BOOKMARKED", receiverId, actorId, bookId, null, book.getTitle(), null);
    }

    public void onBookCommented(Long bookId, Long actorId, Long commentId, String text) {
        BookPO book = bookJpa.findById(bookId).orElseThrow();
        Long receiverId = book.getRecommender().getId();
        if (receiverId.equals(actorId)) return;
        repo.save("BOOK_COMMENTED", receiverId, actorId, bookId, commentId, book.getTitle(), excerpt(text));
    }

    public void onCommentReplied(Long parentCommentId, Long actorId, Long commentId, String text) {
        if (parentCommentId == null) return;
        CommentPO parent = commentJpa.findById(parentCommentId).orElse(null);
        if (parent == null) return;
        Long receiverId = parent.getUser().getId();
        if (receiverId.equals(actorId)) return;
        BookPO book = parent.getBook();
        repo.save("COMMENT_REPLIED", receiverId, actorId, book.getId(), commentId, book.getTitle(), excerpt(text));
    }

    public void onCommentLiked(Long commentId, Long actorId) {
        CommentPO comment = commentJpa.findById(commentId).orElseThrow();
        Long receiverId = comment.getUser().getId();
        if (receiverId.equals(actorId)) return;
        BookPO book = comment.getBook();
        if (repo.exists("COMMENT_LIKED", receiverId, actorId, book.getId(), commentId)) return;
        repo.save("COMMENT_LIKED", receiverId, actorId, book.getId(), commentId, book.getTitle(), null);
    }

    private String excerpt(String text) {
        if (text == null) return null;
        return text.length() > 40 ? text.substring(0, 40) : text;
    }
}
