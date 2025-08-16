package com.novelgrain.domain.booklist;

import java.time.Instant;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BookList {
    private Long id;
    private Long userId;
    private String name;
    private Instant createdAt;
    private Instant updatedAt;
}
