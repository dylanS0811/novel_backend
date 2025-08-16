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
public class Comment {
    private Long id;

    private Long userId;

    private String userName;

    private String userAvatar;

    private String text;

    private Long parentId;

    private int likes;

    private boolean liked;

    private int repliesCount;

    private List<Comment> replies;

    private Instant createdAt;
}
