package com.novelgrain.infrastructure.jpa.entity;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "book", uniqueConstraints = @UniqueConstraint(columnNames = {"title", "author"})) // ← 这里改回 book
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookPO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String title;

    @Column(length = 80)
    private String author;

    @Column(nullable = false, length = 20)
    private String orientation;

    @Column(nullable = false, length = 20)
    private String category;

    @Column(length = 200)
    private String blurb;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String summary;

    @Column(name = "cover_url")
    private String coverUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recommender_id", nullable = false)
    private UserPO recommender;

    @Column(name = "likes_count")
    private Integer likesCount;

    @Column(name = "bookmarks_count")
    private Integer bookmarksCount;

    @Column(name = "comments_count")
    private Integer comments;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToMany
    @JoinTable(
            name = "book_tag",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    @Builder.Default
    private Set<TagPO> tags = new HashSet<>();
}
