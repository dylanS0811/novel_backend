package com.novelgrain.infrastructure.jpa.repo;

import com.novelgrain.infrastructure.jpa.entity.CommentPO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentJpa extends JpaRepository<CommentPO, Long> {
    Page<CommentPO> findByBook_IdAndParentIsNullOrderByCreatedAtDesc(Long bookId, Pageable pageable);

    List<CommentPO> findByParent_IdOrderByCreatedAtAsc(Long parentId);
}
