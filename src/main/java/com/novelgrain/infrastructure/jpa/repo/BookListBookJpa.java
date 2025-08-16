package com.novelgrain.infrastructure.jpa.repo;

import com.novelgrain.infrastructure.jpa.entity.BookListBookPO;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BookListBookJpa extends JpaRepository<BookListBookPO, Long> {
    List<BookListBookPO> findByBookList_Id(Long bookListId);

    boolean existsByBookList_IdAndBookId(Long bookListId, Long bookId);

    @Query(
            """
            SELECT (COUNT(b) > 0) FROM BookListBookPO b
            WHERE b.bookList.id = :bookListId
              AND COALESCE(b.title, '') = :title
              AND COALESCE(b.author, '') = :author
              AND COALESCE(b.orientation, '') = :orientation
              AND COALESCE(b.category, '') = :category
            """
    )
    boolean existsByBookListIdAndTitleAndAuthorAndOrientationAndCategory(
            @Param("bookListId") Long bookListId,
            @Param("title") String title,
            @Param("author") String author,
            @Param("orientation") String orientation,
            @Param("category") String category);
}
