package com.novelgrain.domain.book;

import java.util.Set;

/**
 * Allowed book orientations used across the application.
 */
public final class BookOrientations {
    private BookOrientations() {}

    public static final Set<String> ALL = Set.of(
            "BG",
            "BL",
            "GL",
            "无CP",
            "男频",
            "女频"
    );
}
