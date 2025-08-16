package com.novelgrain.domain.notification;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NotificationItem {
    private Long id;

    private String type;

    private String title;

    private String content;

    private Long bookId;

    private Long fromUserId;

    private String fromUserName;

    private String fromUserAvatar;

    private boolean read;

    private Instant createdAt;
}
