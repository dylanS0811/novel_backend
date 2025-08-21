package com.novelgrain.domain.tag;

public interface TagRepository {
    org.springframework.data.domain.Page<Tag> page(String search, int page, int size, String sort);

    java.util.List<String> suggest(String q);

    String createIfAbsent(String name);
}
