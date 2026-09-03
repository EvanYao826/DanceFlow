package com.danceflow.controller;

import com.danceflow.common.Result;
import com.danceflow.service.FileStorageService;
import com.danceflow.vo.FileUploadVO;
import jakarta.validation.constraints.NotNull;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/files")
public class FileController {
    private final FileStorageService storageService;

    public FileController(FileStorageService storageService) { this.storageService = storageService; }

    @PostMapping("/upload")
    @PreAuthorize("isAuthenticated()")
    public Result<FileUploadVO> upload(@RequestPart("file") @NotNull MultipartFile file) {
        return Result.ok(storageService.store(file));
    }
}
