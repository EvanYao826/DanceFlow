package com.danceflow.controller;

import com.danceflow.common.PageResult;
import com.danceflow.common.Result;
import com.danceflow.dto.ActivityRequest;
import com.danceflow.dto.ActivityStatusRequest;
import com.danceflow.dto.ActivityApplyRequest;
import com.danceflow.service.ActivityApplyService;
import com.danceflow.vo.ActivityApplyVO;
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
    private final ActivityApplyService applyService;

    public ActivityController(ActivityService activityService, ActivityApplyService applyService) { this.activityService = activityService; this.applyService = applyService; }

    @GetMapping("/activities")
    public Result<PageResult<ActivityVO>> page(@RequestParam(defaultValue = "1") long page, @RequestParam(defaultValue = "10") long pageSize) {
        return Result.ok(activityService.publicPage(page, pageSize));
    }

    @GetMapping("/activities/{id}")
    public Result<ActivityVO> detail(Authentication authentication, @PathVariable Long id) {
        Long userId = authentication != null && authentication.getPrincipal() instanceof AuthUser user ? user.id() : null;
        return Result.ok(activityService.detail(id, true, userId));
    }

    @GetMapping("/admin/activities/{id}")
    public Result<ActivityVO> adminDetail(@PathVariable Long id) { return Result.ok(activityService.detail(id, false)); }

    @PostMapping("/activities/{id}/apply")
    public Result<ActivityApplyVO> apply(Authentication authentication, @PathVariable Long id, @Valid @RequestBody(required = false) ActivityApplyRequest request) {
        return Result.ok(applyService.apply(((AuthUser) authentication.getPrincipal()).id(), id, request));
    }

    @DeleteMapping("/activities/{id}/apply")
    public Result<Void> cancel(Authentication authentication, @PathVariable Long id) {
        applyService.cancel(((AuthUser) authentication.getPrincipal()).id(), id); return Result.ok();
    }

    @GetMapping("/activities/my")
    public Result<java.util.List<ActivityApplyVO>> mine(Authentication authentication) {
        return Result.ok(applyService.mine(((AuthUser) authentication.getPrincipal()).id()));
    }

    @GetMapping("/admin/activities/{id}/applications")
    public Result<java.util.List<ActivityApplyVO>> applications(@PathVariable Long id) {
        return Result.ok(applyService.forActivity(id));
    }

    @GetMapping("/admin/activities")
    public Result<PageResult<ActivityVO>> adminPage(@RequestParam(defaultValue = "1") long page, @RequestParam(defaultValue = "10") long pageSize) {
        return Result.ok(activityService.adminPage(page, pageSize));
    }

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
