package com.danceflow.controller;

import com.danceflow.common.PageResult;
import com.danceflow.common.Result;
import com.danceflow.dto.NoticeRequest;
import com.danceflow.dto.NoticeStatusRequest;
import com.danceflow.security.AuthUser;
import com.danceflow.service.NoticeService;
import com.danceflow.vo.NoticeVO;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController @RequestMapping("/api")
public class NoticeController {
    private final NoticeService noticeService;
    public NoticeController(NoticeService noticeService) { this.noticeService = noticeService; }
    @GetMapping("/notices") public Result<List<NoticeVO>> list() { return Result.ok(noticeService.publicList()); }
    @GetMapping("/notices/{id}") public Result<NoticeVO> detail(@PathVariable Long id) { return Result.ok(noticeService.publicDetail(id)); }
    @GetMapping("/admin/notices") public Result<PageResult<NoticeVO>> page(@RequestParam(defaultValue="1") long page,@RequestParam(defaultValue="10") long pageSize,@RequestParam(required=false) String status) { return Result.ok(noticeService.adminPage(page,pageSize,status)); }
    @PostMapping("/admin/notices") public Result<NoticeVO> create(Authentication auth,@Valid @RequestBody NoticeRequest request) { return Result.ok(noticeService.create(((AuthUser)auth.getPrincipal()).id(),request)); }
    @PutMapping("/admin/notices/{id}") public Result<NoticeVO> update(@PathVariable Long id,@Valid @RequestBody NoticeRequest request) { return Result.ok(noticeService.update(id,request)); }
    @PutMapping("/admin/notices/{id}/status") public Result<NoticeVO> status(@PathVariable Long id,@Valid @RequestBody NoticeStatusRequest request) { return Result.ok(noticeService.updateStatus(id,request)); }
}
