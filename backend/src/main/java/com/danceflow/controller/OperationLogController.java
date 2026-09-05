package com.danceflow.controller;

import com.danceflow.common.PageResult;
import com.danceflow.common.Result;
import com.danceflow.service.OperationLogService;
import com.danceflow.vo.OperationLogVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController @RequestMapping("/api/admin/logs")
public class OperationLogController {
    private final OperationLogService service;
    public OperationLogController(OperationLogService service) { this.service = service; }
    @GetMapping public Result<PageResult<OperationLogVO>> page(@RequestParam(defaultValue = "1") long page, @RequestParam(defaultValue = "20") long pageSize, @RequestParam(required = false) String keyword, @RequestParam(required = false) String result) { return Result.ok(service.page(page, pageSize, keyword, result)); }
}
