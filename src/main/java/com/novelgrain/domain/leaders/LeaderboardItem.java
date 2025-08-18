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

    /**
     * User nickname. Historically this API returned the field name as
     * {@code name}.  Frontend navigation now relies on an explicit
     * {@code nick} field, so we expose it directly to avoid ambiguity when
     * constructing user URLs.
     */
    private String nick;

    private String avatar;

    private long score;
}
