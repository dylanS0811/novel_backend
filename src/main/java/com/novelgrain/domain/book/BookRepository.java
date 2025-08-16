package com.novelgrain.domain.book;

import org.springframework.data.domain.Page;

public interface BookRepository {
    Page<Book> page(String tab, String category, String orientation, String search, String tag, int page, int size);

    Book findById(Long id);

    Book save(Book book, Long recommenderId);

    Book update(Long id, Book patch);

    void delete(Long id, Long requesterId);

    void like(Long bookId, Long userId);

    void unlike(Long bookId, Long userId);

    void bookmark(Long bookId, Long userId);

    void unbookmark(Long bookId, Long userId);

    Comment addComment(Long bookId, Long userId, String text, Long parentId);

    Page<Comment> comments(Long bookId, Long userId, int page, int size);

    Comment likeComment(Long commentId, Long userId);

    Comment unlikeComment(Long commentId, Long userId);

    Comment findComment(Long commentId, Long userId);

    boolean existsByTitleAndAuthor(String title, String author);
}
