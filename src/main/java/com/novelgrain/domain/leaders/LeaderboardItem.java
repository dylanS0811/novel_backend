package com.novelgrain.domain.leaders;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LeaderboardItem {
    private int rank;

    private String name;

    private String avatar;

    private long score;
}
