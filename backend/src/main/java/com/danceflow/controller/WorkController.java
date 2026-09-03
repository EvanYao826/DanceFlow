package com.danceflow.controller;

import com.danceflow.common.PageResult;
import com.danceflow.common.Result;
import com.danceflow.dto.WorkAuditRequest;
import com.danceflow.dto.WorkCommentRequest;
import com.danceflow.dto.WorkRequest;
import com.danceflow.security.AuthUser;
import com.danceflow.service.WorkService;
import com.danceflow.vo.WorkActionVO;
import com.danceflow.vo.WorkCommentVO;
import com.danceflow.vo.WorkVO;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api")
public class WorkController {
    private final WorkService workService;
    public WorkController(WorkService workService) { this.workService = workService; }

    @GetMapping("/works")
    public Result<PageResult<WorkVO>> page(Authentication authentication, @RequestParam(defaultValue = "1") long page,
                                           @RequestParam(defaultValue = "12") long pageSize, @RequestParam(required = false) String danceType,
                                           @RequestParam(required = false) String sortBy) {
        return Result.ok(workService.publicPage(page, pageSize, danceType, sortBy, userId(authentication)));
    }

    @GetMapping("/works/{id}")
    public Result<WorkVO> detail(Authentication authentication, @PathVariable Long id) { return Result.ok(workService.detail(id, userId(authentication), true)); }
    @PostMapping("/works")
    public Result<WorkVO> create(Authentication authentication, @Valid @RequestBody WorkRequest request) { return Result.ok(workService.create(currentId(authentication), request)); }
    @PutMapping("/works/{id}")
    public Result<WorkVO> update(Authentication authentication, @PathVariable Long id, @Valid @RequestBody WorkRequest request) { return Result.ok(workService.update(id, currentId(authentication), isAdmin(authentication), request)); }
    @DeleteMapping("/works/{id}")
    public Result<Void> delete(Authentication authentication, @PathVariable Long id) { workService.delete(id, currentId(authentication), isAdmin(authentication)); return Result.ok(); }
    @GetMapping("/works/mine")
    public Result<PageResult<WorkVO>> mine(Authentication authentication, @RequestParam(defaultValue = "1") long page, @RequestParam(defaultValue = "12") long pageSize) { return Result.ok(workService.mine(page, pageSize, currentId(authentication))); }
    @PostMapping("/works/{id}/like")
    public Result<WorkActionVO> like(Authentication authentication, @PathVariable Long id) { return Result.ok(workService.toggleLike(id, currentId(authentication))); }
    @PostMapping("/works/{id}/collection")
    public Result<WorkActionVO> collection(Authentication authentication, @PathVariable Long id) { return Result.ok(workService.toggleCollection(id, currentId(authentication))); }
    @GetMapping("/works/{id}/comments")
    public Result<List<WorkCommentVO>> comments(@PathVariable Long id) { return Result.ok(workService.comments(id)); }
    @PostMapping("/works/{id}/comments")
    public Result<WorkCommentVO> comment(Authentication authentication, @PathVariable Long id, @Valid @RequestBody WorkCommentRequest request) { return Result.ok(workService.comment(id, currentId(authentication), request)); }
    @DeleteMapping("/comments/{id}")
    public Result<Void> deleteComment(Authentication authentication, @PathVariable Long id) { workService.deleteComment(id, currentId(authentication), isAdmin(authentication)); return Result.ok(); }

    @GetMapping("/admin/works")
    public Result<PageResult<WorkVO>> adminPage(@RequestParam(defaultValue = "1") long page, @RequestParam(defaultValue = "12") long pageSize, @RequestParam(required = false) String status) { return Result.ok(workService.adminPage(page, pageSize, status)); }
    @GetMapping("/admin/works/{id}")
    public Result<WorkVO> adminDetail(@PathVariable Long id) { return Result.ok(workService.detail(id, null, false)); }
    @PutMapping("/admin/works/{id}/audit")
    public Result<WorkVO> audit(@PathVariable Long id, @Valid @RequestBody WorkAuditRequest request) { return Result.ok(workService.audit(id, request)); }

    private Long currentId(Authentication a) { return ((AuthUser) a.getPrincipal()).id(); }
    private Long userId(Authentication a) { return a != null && a.getPrincipal() instanceof AuthUser user ? user.id() : null; }
    private boolean isAdmin(Authentication a) { return a.getPrincipal() instanceof AuthUser user && Set.of("ADMIN", "SUPER_ADMIN").contains(user.role()); }
}
