package com.novelgrain.infrastructure.adapter;

import com.novelgrain.domain.tag.TagRepository;
import com.novelgrain.domain.tag.Tag;
import com.novelgrain.infrastructure.jpa.entity.TagPO;
import com.novelgrain.infrastructure.jpa.repo.TagJpa;
import com.novelgrain.infrastructure.jpa.repo.BookJpa;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TagRepositoryJpaAdapter implements TagRepository {
    private final TagJpa tagJpa;
    private final BookJpa bookJpa;

    @Override
    public Page<Tag> page(String search, int page, int size, String sort) {
        var all = tagJpa.findAll();
        List<Tag> tags = all.stream()
                .filter(t -> search == null || search.isBlank() || t.getName().toLowerCase().contains(search.toLowerCase()))
                .map(t -> Tag.builder().id(t.getId()).name(t.getName())
                        .hot(bookJpa.countByTags_Id(t.getId())).build())
                .toList();
        if ("hot".equalsIgnoreCase(sort)) {
            tags = tags.stream().sorted(Comparator.comparing(Tag::getHot).reversed().thenComparing(Tag::getName)).toList();
        } else {
            tags = tags.stream().sorted(Comparator.comparing(Tag::getName)).toList();
        }
        int from = Math.max(0, (page - 1) * size);
        int to = Math.min(tags.size(), from + size);
        var sub = tags.subList(from, to);
        return new PageImpl<>(sub, PageRequest.of(page - 1, size), tags.size());
    }

    @Override
    public java.util.List<String> suggest(String q) {
        return tagJpa.findTop10ByNameContainingIgnoreCaseOrderByNameAsc(q).stream().map(TagPO::getName).toList();
    }

    @Override
    public String createIfAbsent(String name) {
        return tagJpa.findByName(name)
                .orElseGet(() -> tagJpa.save(TagPO.builder().name(name).createdAt(LocalDateTime.now()).build()))
                .getName();
    }
}
