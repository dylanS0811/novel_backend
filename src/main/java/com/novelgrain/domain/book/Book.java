package com.novelgrain.domain.book;

import java.time.Instant;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Book {
    private Long id;

    private String title;

    private String author;

    private String orientation;

    private String category;

    private String blurb;

    private String summary;

    private String coverUrl;

    private Instant createdAt;

    private Instant editableUntil;

    private Integer likes;

    private Integer bookmarks;

    private Integer comments;

    private Recommender recommender;

    private List<String> tags;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Recommender {
        private Long id;

        private String name;

        private String avatar;

        private Integer count;
    }
}
