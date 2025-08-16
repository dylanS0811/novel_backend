package com.novelgrain.domain.leaders;

public interface LeaderboardRepository {
    java.util.List<LeaderboardItem> topUsers(String type, int limit);
}
