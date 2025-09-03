package com.novelgrain.domain.book;

import java.util.Set;

/**
 * Allowed book orientations used across the application.
 *
 * <p>The values mirror the constants defined on the frontend to ensure
 * that uploads and updates accept every orientation option presented to
 * users.</p>
 */
public final class BookOrientations {
    private BookOrientations() {}

    public static final Set<String> ALL = Set.of(
            "BL主受",
            "BL主攻",
            "言情",
            "男主无CP",
            "女主无CP",
            "男频",
            "女频",
            "其他"
    );

    public static boolean isValid(String value) {
        return value != null && ALL.contains(value);
    }
}
