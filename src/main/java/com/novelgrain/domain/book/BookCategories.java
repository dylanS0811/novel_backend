package com.novelgrain.domain.book;

import java.util.Set;

/**
 * Allowed book categories used across the application.
 */
public final class BookCategories {
    private BookCategories() {}

    public static final Set<String> ALL = Set.of(
            "爱情",
            "剧情",
            "都市",
            "历史",
            "奇幻",
            "仙侠",
            "同人",
            "海棠",
            "酸涩",
            "职场",
            "无限流",
            "快穿",
            "游戏",
            "科幻",
            "童话",
            "惊悚",
            "悬疑",
            "年代"
    );
}
