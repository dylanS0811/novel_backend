package com.novelgrain.infrastructure.adapter;

import com.novelgrain.domain.leaders.LeaderboardItem;
import com.novelgrain.domain.leaders.LeaderboardRepository;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import org.springframework.stereotype.Component;

@Component
public class LeaderboardRepositorySqlAdapter implements LeaderboardRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    @SuppressWarnings("unchecked")
    public List<LeaderboardItem> topUsers(String type, int limit) {
        boolean rookie = "rookie".equalsIgnoreCase(type);

        // add table-qualified time filters to avoid "Column 'created_at' ... is ambiguous"
        String cTime = rookie ? " AND c.created_at  >= DATE_SUB(NOW(), INTERVAL 30 DAY) " : "";
        String bmTime = rookie ? " AND bm.created_at >= DATE_SUB(NOW(), INTERVAL 30 DAY) " : "";
        String lTime = rookie ? " AND l.created_at  >= DATE_SUB(NOW(), INTERVAL 30 DAY) " : "";

        String sql =
                "SELECT u.nick, u.avatar, " +
                        "       (SELECT COUNT(*) " +
                        "          FROM comment c " +
                        "          JOIN book b ON b.id = c.book_id " +
                        "         WHERE b.recommender_id = u.id " + cTime + ") * 1 " +
                        "     + (SELECT COUNT(*) " +
                        "          FROM book_bookmark bm " +
                        "          JOIN book b ON b.id = bm.book_id " +
                        "         WHERE b.recommender_id = u.id " + bmTime + ") * 2 " +
                        "     + (SELECT COUNT(*) " +
                        "          FROM book_like l " +
                        "          JOIN book b ON b.id = l.book_id " +
                        "         WHERE b.recommender_id = u.id " + lTime + ") * 3 AS score " +
                        "FROM `user` u " +                       // backticks in case 'user' is reserved
                        "ORDER BY score DESC " +
                        "LIMIT :limit";

        List<Object[]> rows = em.createNativeQuery(sql)
                .setParameter("limit", limit)
                .getResultList();

        List<LeaderboardItem> list = new ArrayList<>();
        int rank = 1;
        for (Object[] r : rows) {
            list.add(LeaderboardItem.builder()
                    .rank(rank++)
                    // expose user's nickname so the frontend can link to
                    // their profile reliably
                    .nick((String) r[0])
                    .avatar((String) r[1])
                    .score(((Number) r[2]).longValue())
                    .build());
        }
        return list;
    }
}
