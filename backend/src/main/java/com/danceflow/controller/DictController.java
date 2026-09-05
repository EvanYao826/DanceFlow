package com.danceflow.controller;

import com.danceflow.common.Result;
import com.danceflow.dto.DictUpdateRequest;
import com.danceflow.service.DictService;
import com.danceflow.vo.DictVO;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

@RestController @RequestMapping("/api/admin/dicts")
public class DictController {
    private final DictService service;
    public DictController(DictService service) { this.service = service; }
    @GetMapping("/{type}") public Result<List<DictVO>> list(@PathVariable String type) { return Result.ok(service.list(type)); }
    @PreAuthorize("hasRole('SUPER_ADMIN')") @PutMapping("/{id}") public Result<DictVO> update(@PathVariable Long id, @Valid @RequestBody DictUpdateRequest request) { return Result.ok(service.update(id, request)); }
}
