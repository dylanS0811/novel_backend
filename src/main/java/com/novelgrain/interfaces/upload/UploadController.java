package com.novelgrain.interfaces.upload;

import com.novelgrain.common.ApiResponse;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/uploads")
public class UploadController {

    private static final long MAX_SIZE = 2 * 1024 * 1024; // 2MB

    @PostMapping(value = "/avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<?> uploadAvatar(@RequestParam("file") MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "未选择文件");
        }
        if (file.getSize() > MAX_SIZE) {
            throw new ResponseStatusException(HttpStatus.PAYLOAD_TOO_LARGE, "文件过大");
        }
        String contentType = file.getContentType();
        if (contentType == null || !(MediaType.IMAGE_JPEG_VALUE.equals(contentType)
                || MediaType.IMAGE_PNG_VALUE.equals(contentType))) {
            throw new ResponseStatusException(HttpStatus.UNSUPPORTED_MEDIA_TYPE, "仅支持 JPG/PNG 图片");
        }
        String ext = MediaType.IMAGE_PNG_VALUE.equals(contentType) ? ".png" : ".jpg";
        String filename = UUID.randomUUID().toString() + ext;
        Path uploadDir = Paths.get("uploads");
        try {
            Files.createDirectories(uploadDir);
            Path dest = uploadDir.resolve(filename);
            file.transferTo(dest);
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "保存文件失败");
        }
        return ApiResponse.ok(Map.of("url", "/uploads/" + filename));
    }
}
