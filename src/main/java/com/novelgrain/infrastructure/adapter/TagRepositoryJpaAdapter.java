package com.novelgrain.infrastructure.adapter;

import com.novelgrain.domain.tag.TagRepository;
import com.novelgrain.infrastructure.jpa.entity.TagPO;
import com.novelgrain.infrastructure.jpa.repo.TagJpa;

import java.time.LocalDateTime;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TagRepositoryJpaAdapter implements TagRepository {
    private final TagJpa tagJpa;

    @Override
    public java.util.List<String> suggest(String q) {
        return tagJpa.findTop10ByNameContainingIgnoreCaseOrderByNameAsc(q).stream().map(TagPO::getName).toList();
    }

    @Override
    public String createIfAbsent(String name) {
        return tagJpa.findByName(name).orElseGet(() -> tagJpa.save(TagPO.builder().name(name).createdAt(LocalDateTime.now()).build())).getName();
    }
}
