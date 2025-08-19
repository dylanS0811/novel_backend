package com.novelgrain.domain.booklist;

import java.time.Instant;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BookListBook {
    private Long id;
    private Long bookListId;
    private Long bookId;
    private String title;
    private String author;
    private String orientation;
    private String category;
    private Integer rating;
    private String review;
    private String summary;
    private List<String> tags;
    private Instant createdAt;
    private Instant updatedAt;
}
