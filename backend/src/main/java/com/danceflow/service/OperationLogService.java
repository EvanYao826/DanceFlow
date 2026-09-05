package com.danceflow.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.danceflow.common.PageResult;
import com.danceflow.entity.OperationLog;
import com.danceflow.mapper.OperationLogMapper;
import com.danceflow.vo.OperationLogVO;
import org.springframework.stereotype.Service;

@Service
public class OperationLogService {
    private final OperationLogMapper mapper;
    public OperationLogService(OperationLogMapper mapper) { this.mapper = mapper; }
    public void record(Long operatorId, String operatorName, String action, String path, String result, String detail) {
        OperationLog log = new OperationLog(); log.setOperatorId(operatorId); log.setOperatorName(operatorName); log.setAction(action);
        log.setRequestPath(path); log.setResult(result); log.setDetail(detail); mapper.insert(log);
    }
    public PageResult<OperationLogVO> page(long page, long pageSize, String keyword, String result) {
        Page<OperationLog> data = mapper.selectPage(new Page<>(Math.max(1, page), Math.min(Math.max(1, pageSize), 100)),
                new LambdaQueryWrapper<OperationLog>().and(keyword != null && !keyword.isBlank(), q -> q.like(OperationLog::getOperatorName, keyword).or().like(OperationLog::getRequestPath, keyword).or().like(OperationLog::getDetail, keyword))
                        .eq(result != null && !result.isBlank(), OperationLog::getResult, result).orderByDesc(OperationLog::getCreatedAt));
        return new PageResult<>(data.getRecords().stream().map(OperationLogVO::from).toList(), data.getTotal(), data.getCurrent(), data.getSize());
    }
}
