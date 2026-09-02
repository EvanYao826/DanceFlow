package com.danceflow.controller;

import com.danceflow.common.PageResult;
import com.danceflow.common.Result;
import com.danceflow.dto.ActivityRequest;
import com.danceflow.dto.ActivityStatusRequest;
import com.danceflow.security.AuthUser;
import com.danceflow.service.ActivityService;
import com.danceflow.vo.ActivityVO;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class ActivityController {
    private final ActivityService activityService;

    public ActivityController(ActivityService activityService) { this.activityService = activityService; }

    @GetMapping("/activities")
    public Result<PageResult<ActivityVO>> page(@RequestParam(defaultValue = "1") long page, @RequestParam(defaultValue = "10") long pageSize) {
        return Result.ok(activityService.publicPage(page, pageSize));
    }

    @GetMapping("/activities/{id}")
    public Result<ActivityVO> detail(@PathVariable Long id) { return Result.ok(activityService.detail(id, true)); }

    @PostMapping("/admin/activities")
    public Result<ActivityVO> create(Authentication authentication, @Valid @RequestBody ActivityRequest request) {
        return Result.ok(activityService.create(((AuthUser) authentication.getPrincipal()).id(), request));
    }

    @PutMapping("/admin/activities/{id}")
    public Result<ActivityVO> update(@PathVariable Long id, @Valid @RequestBody ActivityRequest request) { return Result.ok(activityService.update(id, request)); }

    @PutMapping("/admin/activities/{id}/status")
    public Result<ActivityVO> status(@PathVariable Long id, @Valid @RequestBody ActivityStatusRequest request) { return Result.ok(activityService.updateStatus(id, request)); }

    @DeleteMapping("/admin/activities/{id}")
    public Result<Void> delete(@PathVariable Long id) { activityService.delete(id); return Result.ok(); }
}
