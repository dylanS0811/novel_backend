package com.novelgrain.domain.book;

import java.util.Set;

/**
 * Allowed book orientations used across the application.
 *
 * <p>The original implementation only accepted a fixed set of exact
 * values. However, the frontend may send more specific orientations such
 * as {@code "BL主受"}.  To keep compatibility while still preventing
 * completely invalid values, we now validate based on well known
 * prefixes. Any orientation that starts with one of the prefixes is
 * considered valid.</p>
 */
public final class BookOrientations {
    private BookOrientations() {}

    private static final Set<String> PREFIXES = Set.of(
            "BG",
            "BL",
            "GL",
            "无CP",
            "男频",
            "女频"
    );

    public static boolean isValid(String value) {
        if (value == null) return false;
        return PREFIXES.stream().anyMatch(value::startsWith);
    }
}
