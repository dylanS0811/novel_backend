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
    public List<LeaderboardItem> topUsers(String type, int limit) {
        String timeFilter = "rookie".equalsIgnoreCase(type) ? " AND created_at >= DATE_SUB(NOW(), INTERVAL 30 DAY) " : "";
        String sql = ""
                + "SELECT u.nick, u.avatar, "
                + "       (SELECT COUNT(*) FROM book_like l JOIN book b ON b.id=l.book_id WHERE b.recommender_id=u.id " + timeFilter + ") * 1 "
                + "     + (SELECT COUNT(*) FROM book_bookmark bm JOIN book b ON b.id=bm.book_id WHERE b.recommender_id=u.id " + timeFilter + ") * 2 "
                + "     + (SELECT COUNT(*) FROM comment c JOIN book b ON b.id=c.book_id WHERE b.recommender_id=u.id " + timeFilter + ") * 3 AS score "
                + "FROM user u "
                + "ORDER BY score DESC "
                + "LIMIT :limit";
        var rows = em.createNativeQuery(sql).setParameter("limit", limit).getResultList();
        var list = new ArrayList<LeaderboardItem>();
        int rank = 1;
        for (Object row : rows) {
            Object[] r = (Object[]) row;
            list.add(LeaderboardItem.builder().rank(rank++).name((String) r[0]).avatar((String) r[1]).score(((Number) r[2]).longValue()).build());
        }
        return list;
    }
}
