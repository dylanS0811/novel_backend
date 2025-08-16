package com.novelgrain.domain.tag;

public interface TagRepository {
    java.util.List<String> suggest(String q);

    String createIfAbsent(String name);
}
