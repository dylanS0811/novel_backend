package com.novelgrain.application.tag;

import com.novelgrain.domain.tag.TagRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TagUseCases {
    private final TagRepository tagRepo;

    public java.util.List<String> suggest(String q) {
        return tagRepo.suggest(q);
    }

    public String create(String name) {
        return tagRepo.createIfAbsent(name);
    }
}
