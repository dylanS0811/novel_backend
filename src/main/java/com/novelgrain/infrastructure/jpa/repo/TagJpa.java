package com.novelgrain.infrastructure.jpa.repo;

import com.novelgrain.infrastructure.jpa.entity.TagPO;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TagJpa extends JpaRepository<TagPO, Long> {
    Optional<TagPO> findByName(String name);

    List<TagPO> findTop10ByNameContainingIgnoreCaseOrderByNameAsc(String q);
}
