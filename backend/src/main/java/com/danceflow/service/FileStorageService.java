package com.danceflow.service;

import com.danceflow.vo.FileUploadVO;
import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {
    FileUploadVO store(MultipartFile file);
}
