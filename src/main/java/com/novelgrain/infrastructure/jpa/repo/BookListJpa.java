package com.novelgrain.infrastructure.jpa.repo;

import com.novelgrain.infrastructure.jpa.entity.BookListPO;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookListJpa extends JpaRepository<BookListPO, Long> {
    List<BookListPO> findByUser_Id(Long userId);
}
