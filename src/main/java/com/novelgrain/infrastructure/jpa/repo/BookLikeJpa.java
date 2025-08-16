package com.novelgrain.infrastructure.jpa.repo;

import com.novelgrain.infrastructure.jpa.entity.BookLikePO;
import com.novelgrain.infrastructure.jpa.entity.LikeBookmarkKey;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BookLikeJpa extends JpaRepository<BookLikePO, LikeBookmarkKey> {

    // 仍可用关联路径
    boolean existsByUser_IdAndBook_Id(Long userId, Long bookId);

    void deleteByUser_IdAndBook_Id(Long userId, Long bookId);

    @Query("select bl.book.id from BookLikePO bl where bl.user.id = :userId")
    List<Long> findBookIdsByUserId(@Param("userId") Long userId);
}
