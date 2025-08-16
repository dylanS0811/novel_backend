package com.novelgrain.infrastructure.jpa.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.*;

@Entity
@Table(name = "book_list_books")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookListBookPO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_list_id", nullable = false)
    private BookListPO bookList;

    @Column(name = "book_id")
    private Long bookId;

    @Column(nullable = false, length = 120)
    private String title;

    @Column(length = 80)
    private String author;

    @Column(length = 20)
    private String orientation;

    @Column(length = 20)
    private String category;

    private Integer rating;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String review;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    public void preInsert() {
        var now = LocalDateTime.now();
        createdAt = now;
        updatedAt = now;
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
