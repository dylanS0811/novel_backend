package com.novelgrain.interfaces.leaderboard;

import com.novelgrain.application.leaderboard.LeaderboardUseCases;
import com.novelgrain.common.ApiResponse;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/leaderboard")
@RequiredArgsConstructor
public class LeaderboardController {
    private final LeaderboardUseCases use;

    @GetMapping
    public ApiResponse<Object> top(@RequestParam(defaultValue = "champion") String type, @RequestParam(defaultValue = "10") int limit) {
        return ApiResponse.ok(use.top(type, limit));
    }
}
