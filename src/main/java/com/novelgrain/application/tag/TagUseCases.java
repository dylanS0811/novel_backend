package com.novelgrain.application.tag;

import com.novelgrain.domain.tag.TagRepository;
import com.novelgrain.domain.tag.Tag;
import com.novelgrain.common.PageResponse;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TagUseCases {
    private final TagRepository tagRepo;

    public PageResponse<Tag> list(String search, int page, int size, String sort) {
        var p = tagRepo.page(search, page, size, sort);
        return new PageResponse<>(p.getContent(), page, size, p.getTotalElements());
    }

    public java.util.List<String> suggest(String q) {
        return tagRepo.suggest(q);
    }

    public String create(String name) {
        return tagRepo.createIfAbsent(name);
    }
}
