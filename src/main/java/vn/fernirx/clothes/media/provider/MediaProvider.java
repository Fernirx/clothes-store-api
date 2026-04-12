package vn.fernirx.clothes.media.provider;

import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public interface MediaProvider {
    Map<?, ?> upload(MultipartFile file, String folder, String publicId);
    void delete(String publicId);
}
