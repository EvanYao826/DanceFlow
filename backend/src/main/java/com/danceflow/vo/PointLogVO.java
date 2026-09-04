package com.danceflow.vo;
import com.danceflow.entity.UserPointLog;
import java.time.LocalDateTime;
public record PointLogVO(Long id, String pointType, Integer pointValue, String sourceType, Long sourceId, String remark, LocalDateTime createdAt) {
    public static PointLogVO from(UserPointLog log) { return new PointLogVO(log.getId(), log.getPointType(), log.getPointValue(), log.getSourceType(), log.getSourceId(), log.getRemark(), log.getCreatedAt()); }
}
