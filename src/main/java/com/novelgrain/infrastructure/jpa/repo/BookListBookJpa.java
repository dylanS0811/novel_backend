package com.novelgrain.infrastructure.jpa.repo;

import com.novelgrain.infrastructure.jpa.entity.BookListBookPO;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookListBookJpa extends JpaRepository<BookListBookPO, Long> {
    List<BookListBookPO> findByBookList_Id(Long bookListId);
}
