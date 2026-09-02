package com.danceflow.controller;

import com.danceflow.common.PageResult;
import com.danceflow.common.Result;
import com.danceflow.dto.MemberApplyRequest;
import com.danceflow.dto.MemberAuditRequest;
import com.danceflow.dto.MemberStatusRequest;
import com.danceflow.dto.MemberUpdateRequest;
import com.danceflow.security.AuthUser;
import com.danceflow.service.MemberService;
import com.danceflow.vo.MemberVO;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/members")
public class MemberController {
    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/apply")
    public Result<MemberVO> apply(Authentication authentication, @Valid @RequestBody MemberApplyRequest request) {
        return Result.ok(memberService.apply(currentUserId(authentication), request));
    }

    @GetMapping("/me")
    public Result<MemberVO> me(Authentication authentication) {
        return Result.ok(memberService.me(currentUserId(authentication)));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public Result<PageResult<MemberVO>> page(@RequestParam(defaultValue = "1") long page, @RequestParam(defaultValue = "10") long pageSize) {
        return Result.ok(memberService.page(page, pageSize));
    }

    @GetMapping("/{id}")
    public Result<MemberVO> detail(@PathVariable Long id) {
        return Result.ok(memberService.detail(id));
    }

    @PutMapping("/{id}")
    public Result<MemberVO> update(Authentication authentication, @PathVariable Long id, @Valid @RequestBody MemberUpdateRequest request) {
        AuthUser user = currentUser(authentication);
        return Result.ok(memberService.update(id, user.id(), isAdmin(user), request));
    }

    @PutMapping("/{id}/audit")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public Result<MemberVO> audit(@PathVariable Long id, @Valid @RequestBody MemberAuditRequest request) {
        return Result.ok(memberService.audit(id, request));
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public Result<MemberVO> status(@PathVariable Long id, @Valid @RequestBody MemberStatusRequest request) {
        return Result.ok(memberService.status(id, request));
    }

    private AuthUser currentUser(Authentication authentication) {
        return (AuthUser) authentication.getPrincipal();
    }

    private Long currentUserId(Authentication authentication) {
        return currentUser(authentication).id();
    }

    private boolean isAdmin(AuthUser user) {
        return "ADMIN".equals(user.role()) || "SUPER_ADMIN".equals(user.role());
    }
}
