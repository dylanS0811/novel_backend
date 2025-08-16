package com.novelgrain.infrastructure.jpa.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;

@Entity
@Table(name = "book_bookmark")
public class BookBookmarkPO implements Serializable {

    @EmbeddedId
    private LikeBookmarkKey id;

    @MapsId("bookId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", referencedColumnName = "id", nullable = false)
    private BookPO book;

    @MapsId("userId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private UserPO user;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public BookBookmarkPO() {
    }

    public BookBookmarkPO(LikeBookmarkKey id, BookPO book, UserPO user, LocalDateTime createdAt) {
        this.id = id;
        this.book = book;
        this.user = user;
        this.createdAt = createdAt;
    }

    public static BookBookmarkPO of(BookPO book, UserPO user, LocalDateTime now) {
        var key = new LikeBookmarkKey(book.getId(), user.getId());
        return new BookBookmarkPO(key, book, user, now);
    }

    public LikeBookmarkKey getId() {
        return id;
    }

    public void setId(LikeBookmarkKey id) {
        this.id = id;
    }

    public BookPO getBook() {
        return book;
    }

    public void setBook(BookPO book) {
        this.book = book;
    }

    public UserPO getUser() {
        return user;
    }

    public void setUser(UserPO user) {
        this.user = user;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
