package com.novelgrain.infrastructure.jpa.repo;

import com.novelgrain.infrastructure.jpa.entity.NotificationPO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationJpa extends JpaRepository<NotificationPO, Long> {
    Page<NotificationPO> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);

    Page<NotificationPO> findByUserIdAndTypeOrderByCreatedAtDesc(Long userId, String type, Pageable pageable);

    long countByUserIdAndReadFalse(Long userId);

    boolean existsByTypeAndUser_IdAndActor_IdAndBook_IdAndCommentIsNull(String type, Long userId, Long actorId, Long bookId);

    boolean existsByTypeAndUser_IdAndActor_IdAndBook_IdAndComment_Id(String type, Long userId, Long actorId, Long bookId, Long commentId);
}
