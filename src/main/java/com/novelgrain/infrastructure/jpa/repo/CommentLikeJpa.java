package com.novelgrain.infrastructure.jpa.repo;

import com.novelgrain.infrastructure.jpa.entity.CommentLikePO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentLikeJpa extends JpaRepository<CommentLikePO, CommentLikePO.PK> {
    boolean existsByComment_IdAndUser_Id(Long commentId, Long userId);

    void deleteByComment_IdAndUser_Id(Long commentId, Long userId);
}
