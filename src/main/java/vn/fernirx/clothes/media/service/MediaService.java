package vn.fernirx.clothes.media.service;

import org.springframework.web.multipart.MultipartFile;
import vn.fernirx.clothes.media.dto.response.ImageResponse;

public interface MediaService {
    ImageResponse uploadImage(MultipartFile file, String folder);
    void deleteImage(String publicId);
}
