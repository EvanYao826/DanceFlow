package com.danceflow.service;

import com.danceflow.vo.StatisticsOverviewVO;
import com.danceflow.vo.StatisticsRankingVO;
import com.danceflow.vo.StatisticsTrendVO;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class StatisticsService {
    private final JdbcTemplate jdbc;
    public StatisticsService(JdbcTemplate jdbc) { this.jdbc = jdbc; }
    public StatisticsOverviewVO overview() { return new StatisticsOverviewVO(count("sys_user"), count("club_member", "member_status = 'ACTIVE'"), count("activity"), count("course"), count("dance_work"), count("forum_post"), count("work_comment"), count("activity_apply", "apply_status = 'APPLIED'")); }
    public List<StatisticsTrendVO> userTrend(LocalDate start, LocalDate end) { return trend("SELECT DATE(created_at) day, COUNT(*) value FROM sys_user WHERE is_deleted = 0 AND created_at >= ? AND created_at < ? GROUP BY DATE(created_at) ORDER BY day", start, end); }
    public Map<String, Object> activity(LocalDate start, LocalDate end) { Map<String, Object> result = new LinkedHashMap<>(); result.put("published", scalar("SELECT COUNT(*) FROM activity WHERE status = 'PUBLISHED' AND created_at >= ? AND created_at < ?", start, end)); result.put("applications", scalar("SELECT COUNT(*) FROM activity_apply WHERE apply_status = 'APPLIED' AND apply_time >= ? AND apply_time < ?", start, end)); result.put("finished", scalar("SELECT COUNT(*) FROM activity WHERE status = 'FINISHED' AND updated_at >= ? AND updated_at < ?", start, end)); return result; }
    public Map<String, Long> content() { Map<String, Long> result = new LinkedHashMap<>(); result.put("works", count("dance_work", "audit_status = 'PUBLISHED'")); result.put("posts", count("forum_post", "status = 'PUBLISHED'")); result.put("comments", count("work_comment", "status = 'NORMAL'")); result.put("likes", scalar("SELECT COALESCE(SUM(like_count), 0) FROM dance_work", null, null)); result.put("collections", scalar("SELECT COALESCE(SUM(collection_count), 0) FROM dance_work", null, null)); return result; }
    public Map<String, List<StatisticsRankingVO>> ranking() { Map<String, List<StatisticsRankingVO>> result = new LinkedHashMap<>(); result.put("works", jdbc.query("SELECT id, title name, like_count value, dance_type extra FROM dance_work WHERE is_deleted = 0 ORDER BY like_count DESC, view_count DESC LIMIT 5", (rs, n) -> new StatisticsRankingVO(rs.getLong("id"), rs.getString("name"), rs.getLong("value"), rs.getString("extra")))); result.put("users", jdbc.query("SELECT u.id, u.nickname name, COUNT(p.id) value, '帖子' extra FROM sys_user u LEFT JOIN forum_post p ON p.user_id = u.id AND p.is_deleted = 0 WHERE u.is_deleted = 0 GROUP BY u.id, u.nickname ORDER BY value DESC LIMIT 5", (rs, n) -> new StatisticsRankingVO(rs.getLong("id"), rs.getString("name"), rs.getLong("value"), rs.getString("extra")))); return result; }
    private List<StatisticsTrendVO> trend(String sql, LocalDate start, LocalDate end) { return jdbc.query(sql, new Object[]{(start == null ? LocalDate.now().minusDays(29) : start).atStartOfDay(), (end == null ? LocalDate.now() : end).plusDays(1).atStartOfDay()}, (rs, n) -> new StatisticsTrendVO(rs.getString("day"), rs.getLong("value"))); }
    private long count(String table) { return scalar("SELECT COUNT(*) FROM " + table + " WHERE is_deleted = 0", null, null); }
    private long count(String table, String condition) { return scalar("SELECT COUNT(*) FROM " + table + " WHERE is_deleted = 0 AND " + condition, null, null); }
    private long scalar(String sql, LocalDate start, LocalDate end) { if (start == null) return jdbc.queryForObject(sql, Long.class); return jdbc.queryForObject(sql, Long.class, start.atStartOfDay(), end.plusDays(1).atStartOfDay()); }
}
