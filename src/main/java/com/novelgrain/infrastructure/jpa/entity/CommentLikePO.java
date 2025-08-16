package com.novelgrain.infrastructure.jpa.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "comment_like")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@IdClass(CommentLikePO.PK.class)
public class CommentLikePO {
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id")
    private CommentPO comment;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserPO user;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PK implements java.io.Serializable {
        private Long comment;
        private Long user;
    }
}
