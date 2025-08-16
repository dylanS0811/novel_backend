package com.novelgrain.infrastructure.jpa.repo;

import com.novelgrain.infrastructure.jpa.entity.BookBookmarkPO;
import com.novelgrain.infrastructure.jpa.entity.LikeBookmarkKey;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BookBookmarkJpa extends JpaRepository<BookBookmarkPO, LikeBookmarkKey> {

    boolean existsByUser_IdAndBook_Id(Long userId, Long bookId);

    void deleteByUser_IdAndBook_Id(Long userId, Long bookId);

    @Query("select bb.book.id from BookBookmarkPO bb where bb.user.id = :userId")
    List<Long> findBookIdsByUserId(@Param("userId") Long userId);
}
