package com.novelgrain.infrastructure.jpa.repo;

import com.novelgrain.infrastructure.jpa.entity.NotificationPO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationJpa extends JpaRepository<NotificationPO, Long> {
    Page<NotificationPO> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);

    Page<NotificationPO> findByUserIdAndTypeOrderByCreatedAtDesc(Long userId, String type, Pageable pageable);

    long countByUserIdAndReadFalse(Long userId);
}
