package com.danceflow.service;

import com.danceflow.exception.BusinessException;
import com.danceflow.entity.StoredFile;
import com.danceflow.mapper.StoredFileMapper;
import com.danceflow.vo.FileUploadVO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

@Service
public class LocalFileStorageService implements FileStorageService {
    private static final long MAX_SIZE = 100L * 1024 * 1024;
    private static final Set<String> IMAGE_TYPES = Set.of("image/jpeg", "image/png", "image/webp", "image/gif");
    private static final Set<String> VIDEO_TYPES = Set.of("video/mp4", "video/webm", "video/quicktime");
    private final Path root;
    private final StoredFileMapper storedFileMapper;

    public LocalFileStorageService(@Value("${danceflow.upload.dir:./uploads}") String directory, StoredFileMapper storedFileMapper) {
        this.root = Paths.get(directory).toAbsolutePath().normalize();
        this.storedFileMapper = storedFileMapper;
    }

    @Override
    public FileUploadVO store(MultipartFile file) {
        if (file == null || file.isEmpty()) throw new BusinessException("请选择要上传的文件");
        if (file.getSize() > MAX_SIZE) throw new BusinessException("文件大小不能超过 100MB");
        String contentType = file.getContentType() == null ? "" : file.getContentType().toLowerCase(Locale.ROOT);
        if (!IMAGE_TYPES.contains(contentType) && !VIDEO_TYPES.contains(contentType)) {
            throw new BusinessException("仅支持 JPG、PNG、WEBP、GIF、MP4、WEBM 或 MOV 文件");
        }
        String extension = extension(file.getOriginalFilename(), contentType);
        String datePath = LocalDate.now().toString().replace('-', '/');
        String key = datePath + "/" + UUID.randomUUID() + extension;
        Path target = root.resolve(key).normalize();
        if (!target.startsWith(root)) throw new BusinessException("文件路径不合法");
        try {
            Files.createDirectories(target.getParent());
            file.transferTo(target);
        } catch (IOException | IllegalStateException e) {
            throw new BusinessException("文件保存失败，请稍后重试");
        }
        String url = "/uploads/" + key.replace('\\', '/');
        StoredFile storedFile = new StoredFile();
        storedFile.setOriginalName(file.getOriginalFilename());
        storedFile.setStorageKey(key);
        storedFile.setFileUrl(url);
        storedFile.setContentType(contentType);
        storedFile.setFileSize(file.getSize());
        storedFile.setIsDeleted(0);
        storedFileMapper.insert(storedFile);
        return new FileUploadVO(file.getOriginalFilename(), url, key, contentType, file.getSize());
    }

    private String extension(String originalName, String contentType) {
        String extension = StringUtils.getFilenameExtension(originalName);
        if (extension != null && extension.matches("[A-Za-z0-9]{1,8}")) return "." + extension.toLowerCase(Locale.ROOT);
        return switch (contentType) {
            case "image/jpeg" -> ".jpg";
            case "image/png" -> ".png";
            case "image/webp" -> ".webp";
            case "image/gif" -> ".gif";
            case "video/webm" -> ".webm";
            case "video/quicktime" -> ".mov";
            default -> ".mp4";
        };
    }
}
