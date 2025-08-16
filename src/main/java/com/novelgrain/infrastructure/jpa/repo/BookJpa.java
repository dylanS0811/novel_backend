package com.novelgrain.infrastructure.jpa.repo;

import com.novelgrain.infrastructure.jpa.entity.BookPO;

import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BookJpa extends JpaRepository<BookPO, Long>, JpaSpecificationExecutor<BookPO> {

    // 详情页时把 tags 一次性取出，避免懒加载
    @EntityGraph(attributePaths = "tags")
    @Query("select b from BookPO b where b.id = :id")
    Optional<BookPO> findByIdWithTags(@Param("id") Long id);

    boolean existsByTitleAndAuthor(String title, String author);
}
