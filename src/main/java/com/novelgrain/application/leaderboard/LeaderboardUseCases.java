package com.novelgrain.application.leaderboard;

import com.novelgrain.domain.leaders.LeaderboardItem;
import com.novelgrain.domain.leaders.LeaderboardRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LeaderboardUseCases {
    private final LeaderboardRepository repo;

    public java.util.List<LeaderboardItem> top(String type, int limit) {
        return repo.topUsers(type, limit);
    }
}
