package com.novelgrain.application.book;

import com.novelgrain.common.PageResponse;
import com.novelgrain.domain.book.Book;
import com.novelgrain.domain.book.BookRepository;
import com.novelgrain.domain.book.Comment;
import com.novelgrain.infrastructure.jpa.repo.BookBookmarkJpa;
import com.novelgrain.infrastructure.jpa.repo.BookLikeJpa;
import com.novelgrain.application.notification.NotificationService;

import java.util.List;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookUseCases {
    private final BookRepository bookRepo;

    private final BookLikeJpa likeJpa;

    private final BookBookmarkJpa bookmarkJpa;

    private final NotificationService notificationService;

    public PageResponse<Book> list(String tab, String category, String orientation, String search, String tag, int page, int size) {
        Page<Book> p = bookRepo.page(tab, category, orientation, search, tag, page, size);
        return new PageResponse<>(p.getContent(), page, size, p.getTotalElements());
    }

    public Book detail(Long id) {
        return bookRepo.findById(id);
    }

    public Book create(String title, String author, String orientation, String category, String blurb, String summary, java.util.List<String> tags, Long recommenderId) {
        Book b = Book.builder().title(title).author(author).orientation(orientation).category(category).blurb(blurb).summary(summary).tags(tags).build();
        return bookRepo.save(b, recommenderId);
    }

    public Book update(Long id, Book patch) {
        return bookRepo.update(id, patch);
    }

    public void delete(Long id, Long requesterId) {
        bookRepo.delete(id, requesterId);
    }

    public void like(Long id, Long userId) {
        bookRepo.like(id, userId);
        notificationService.onBookLiked(id, userId);
    }

    public void unlike(Long id, Long userId) {
        bookRepo.unlike(id, userId);
    }

    public void bookmark(Long id, Long userId) {
        bookRepo.bookmark(id, userId);
        notificationService.onBookBookmarked(id, userId);
    }

    public void unbookmark(Long id, Long userId) {
        bookRepo.unbookmark(id, userId);
    }

    public PageResponse<Comment> comments(Long id, Long userId, int page, int size) {
        var p = bookRepo.comments(id, userId, page, size);
        return new PageResponse<>(p.getContent(), page, size, p.getTotalElements());
    }

    public Comment addComment(Long id, Long userId, String text, Long parentId) {
        Comment c = bookRepo.addComment(id, userId, text, parentId);
        notificationService.onBookCommented(id, userId, c.getId(), text);
        if (parentId != null) {
            notificationService.onCommentReplied(parentId, userId, c.getId(), text);
        }
        return c;
    }

    public Comment likeComment(Long commentId, Long userId) {
        Comment c = bookRepo.likeComment(commentId, userId);
        notificationService.onCommentLiked(commentId, userId);
        return c;
    }

    public Comment unlikeComment(Long commentId, Long userId) {
        return bookRepo.unlikeComment(commentId, userId);
    }

    public Comment findComment(Long commentId, Long userId) {
        return bookRepo.findComment(commentId, userId);
    }

    /* ===== 初始化高亮：我点赞过/收藏过的书 ID 列表 ===== */
    public List<Long> listLikedBookIds(Long userId) {
        return likeJpa.findBookIdsByUserId(userId);
    }

    public List<Long> listBookmarkedBookIds(Long userId) {
        return bookmarkJpa.findBookIdsByUserId(userId);
    }

    public boolean exists(String title, String author) {
        return bookRepo.existsByTitleAndAuthor(title, author);
    }
}
