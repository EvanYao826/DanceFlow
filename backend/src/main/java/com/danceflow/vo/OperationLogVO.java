package com.danceflow.vo;

import com.danceflow.entity.OperationLog;

import java.time.LocalDateTime;

public record OperationLogVO(Long id, Long operatorId, String operatorName, String action, String targetType,
                             String targetId, String requestPath, String result, String detail, LocalDateTime createdAt) {
    public static OperationLogVO from(OperationLog log) {
        return new OperationLogVO(log.getId(), log.getOperatorId(), log.getOperatorName(), log.getAction(), log.getTargetType(),
                log.getTargetId(), log.getRequestPath(), log.getResult(), log.getDetail(), log.getCreatedAt());
    }
}
