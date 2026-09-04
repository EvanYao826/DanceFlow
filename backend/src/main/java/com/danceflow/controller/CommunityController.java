package com.danceflow.controller;

import com.danceflow.common.PageResult;
import com.danceflow.common.Result;
import com.danceflow.dto.ForumPostRequest;
import com.danceflow.dto.ForumPostStatusRequest;
import com.danceflow.security.AuthUser;
import com.danceflow.service.CommunityService;
import com.danceflow.vo.ForumPostVO;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController @RequestMapping("/api")
public class CommunityController {
    private final CommunityService communityService;
    public CommunityController(CommunityService communityService) { this.communityService = communityService; }
    @GetMapping("/community/posts") public Result<PageResult<ForumPostVO>> page(@RequestParam(defaultValue="1") long page,@RequestParam(defaultValue="10") long pageSize,@RequestParam(required=false) String keyword,@RequestParam(required=false) String category) { return Result.ok(communityService.publicPage(page,pageSize,keyword,category)); }
    @GetMapping("/community/posts/{id}") public Result<ForumPostVO> detail(@PathVariable Long id) { return Result.ok(communityService.detail(id,true)); }
    @PostMapping("/community/posts") public Result<ForumPostVO> create(Authentication auth,@Valid @RequestBody ForumPostRequest request) { return Result.ok(communityService.create(user(auth).id(),request)); }
    @PutMapping("/community/posts/{id}") public Result<ForumPostVO> update(Authentication auth,@PathVariable Long id,@Valid @RequestBody ForumPostRequest request) { AuthUser u=user(auth); return Result.ok(communityService.update(id,u.id(),admin(u),request)); }
    @DeleteMapping("/community/posts/{id}") public Result<Void> delete(Authentication auth,@PathVariable Long id) { AuthUser u=user(auth); communityService.delete(id,u.id(),admin(u)); return Result.ok(); }
    @GetMapping("/admin/posts") public Result<PageResult<ForumPostVO>> adminPage(@RequestParam(defaultValue="1") long page,@RequestParam(defaultValue="10") long pageSize,@RequestParam(required=false) String keyword,@RequestParam(required=false) String status) { return Result.ok(communityService.adminPage(page,pageSize,keyword,status)); }
    @PutMapping("/admin/posts/{id}/status") public Result<ForumPostVO> status(@PathVariable Long id,@Valid @RequestBody ForumPostStatusRequest request) { return Result.ok(communityService.updateStatus(id,request)); }
    private AuthUser user(Authentication auth) { return (AuthUser) auth.getPrincipal(); } private boolean admin(AuthUser user) { return "ADMIN".equals(user.role()) || "SUPER_ADMIN".equals(user.role()); }
}
