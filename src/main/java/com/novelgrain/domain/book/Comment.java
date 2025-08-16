package com.novelgrain.domain.book;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Comment {
    private Long id;

    private Long userId;

    private String userName;

    private String userAvatar;

    private String text;

    private Instant createdAt;
}
